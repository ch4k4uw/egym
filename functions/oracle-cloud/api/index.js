const port = 3000;

const locale = require("locale")
const admin = require('firebase-admin');
const express = require('express');
const Fs = require('fs-extra');
const https = require('https');
const serviceAccount = require('./e-gym.json');

const fbApp = admin.initializeApp({ credential: admin.credential.cert(serviceAccount) });
const db = fbApp.firestore();
const router = express.Router();
const app = express();

//Middlewares {
const localeParserMiddleware = (req, res, next) => {
    const locale = req.headers['accept-language'];
    if (locale !== 'pt_BR') {
        req._suffix = 'en';
    } else {
        req._suffix = 'pt';
    }
    next();
};

const authMiddleware = (req, res, next) => {
    log(`query string:`);
    log(req.query);
    const headerAuth = req.headers.authorization;
    if (ApiKey.keys.find(key => headerAuth === key)) {
        next()
    } else {
        res.status(403).send({ message: "you have no power here\nSaruman" })
    }
};
//}

//Functions {
const isProduction = () => process.env.NODE_ENV === 'production';
const log = (...params) => !isProduction() ? console.log.apply(console, params) : undefined;
const logError = (...params) => console.error.apply(console, params);
//}

//Classes {
class ApiKey {
    static #_apiKey = undefined;
    static get keys() {
        if (!this.#_apiKey) {
            this.#_apiKey = isProduction() ? require('./api-keys.json') : { keys: ['xxxx'] }
        }
        return this.#_apiKey.keys;
    }
}

class DataSource {
    static #_enData = undefined;
    static get enData() {
        if (!this.#_enData) {
            if (Fs.existsSync('./initial-data-en.json')) {
                this.#_enData = require('./initial-data-en.json');
            } else {
                log(`No 'en' data to initialize.`)
            }
        }
        return this.#_enData;
    }
}

class NotFoundError extends Error {
    constructor() { 
        super('I\'m sorry brow, not found:(\nAre you sure about your request?') 
    }
}
//}

//Query String
//  cp: Collection prefix.
//  qs: Query string to be used to find the docs by title. *** if: not next page operation ***
//  tg: Tags to be used to find the exercises type in the 'tags' field array. *** if: not next page operation ***
//  ls: Last found title value. *Required if: get next page*
//  ld: Last found 'created' date value. *Required if: get next page*
//  sz: Page size. **Required**
//  cr: Current page index. **Required**
//  op: Operation (can be empty/undefined or 'next').
//  pc: Page count. *Required if: get next page*.
//  cc: Collection count. *Required if: get next page*.
router.get('/exercise/head', async (req, res) => {
    const collectionPrefix = req.query.cp;
    const collectionName = (collectionPrefix ? `${collectionPrefix}_` : '') + 'exercise' + `_${req._suffix}`;
    const queryString = req.query.qs;
    const queryTags = req.query.tg;
    const lastTitle = req.query.ls;
    const lastCreatedAt = req.query.ld;
    const pageSize = req.query.sz;
    let currentPageIndex = req.query.cr || 0;
    const operation = req.query.op;
    let pageCount = req.query.pc;
    let collectionCount = req.query.cc;

    let isBadRequest = false;
    if (operation === 'next') {
        isBadRequest = !lastTitle || !lastCreatedAt || !pageCount || !collectionCount || queryString || queryTags;
    }
    if (!isBadRequest) {
        isBadRequest = !pageSize;
    }
    if (isBadRequest) {
        res.status(400).send({ message: 'invalid parameter.' });
        return;
    } else {
        try {
            log(`finding in '${collectionName}' collection.`);
            const collection = db.collection(collectionName);
            if (!collectionCount) {
                let query = collection
                .select(admin.firestore.FieldPath.documentId())
                .orderBy('title')
                .orderBy('created');

                if (queryString) {
                    log('Has queryString');
                    query = query
                        .startAt(queryString)
                        .endAt(`${queryString}\uF8FF`);
                }
                if (queryTags) {
                    log('Has queryTags');
                    query = query.where('tags', 'array-contains', queryTags);
                }
                collectionCount = (await query.get()).docs.length;
            }
            if(!pageCount) {
                pageCount = parseInt(collectionCount / pageSize) + (collectionCount % pageSize !== 0? 1 : 0);
            }
            let query = collection
                .select('title', 'images', 'created', 'updated')
                .orderBy('title')
                .orderBy('created');

            let performQuery = true;
            if (queryString || (lastTitle && lastCreatedAt)) {
                if (queryString) {
                    query = query
                        .startAt(queryString)
                        .endAt(`${queryString}\uF8FF`);
                } else {
                    if (currentPageIndex < pageCount) {
                        query = query
                            .startAfter(lastTitle, lastCreatedAt);
                        ++currentPageIndex;
                    } else {
                        performQuery = false;
                    }
                }
            }

            if (queryTags) {
                query = query.where('tags', 'array-contains', queryTags)
            }

            query = query.limit(performQuery ? +pageSize: 0);

            const items = performQuery ? (await query.get()).docs.map(doc => {
                return { 
                    id: doc.id,
                    title: doc.get('title'),
                    images: doc.get('images'),
                    created: doc.get('created'),
                    updated: doc.get('updated')
                };
            }) : [];

            res.status(200).send({
                index: currentPageIndex,
                count: pageCount,
                size: pageSize,
                items,
                itemsCount: collectionCount
            });

        } catch (e) {
            if (!isProduction()) {
                logError(e);
            }
            res.status(500).send({ message: 'internal server error', cause: e || 'unknown' });
        }
    }
});

router.use((req, res, next)=> {
    if (!req.route) {
        return next(new NotFoundError());
    }
    next();
});

router.use((err, _req, res, _next)=> {
    if (err instanceof NotFoundError) {
        res.status(404).send({ message: err.message });
    } else {
        res.status(500).send({ message: err.message });
    }
});

app.use(locale(["en", "en_US", "pt_BR"], "en_US"));
app.use(localeParserMiddleware);
app.use(authMiddleware);
app.use(router);

if (isProduction()) {
    const key = Fs.readFileSync('/certs/privkey1.pem');
    const cert = Fs.readFileSync('/certs/fullchain1.pem');
    https.createServer({ key, cert }, app).listen(port, () => {
        console.log(`Listening at ${port} security port`);
    });
}
else {
    app.listen(port, async () => {
        if (DataSource.enData) {
            const enData = DataSource.enData;
            const batch = db.batch();
            const ref = db.collection('exercise_en');
            const now = new Date();
            for(let i=5; i<enData.length; ++i) {
                const data = enData[i];
                const doc = ref.doc();
                data.created = now.getTime();
                data.updated = now.getTime();
                batch.set(doc, data);
            }
            await batch.commit();
        }
        console.log(`\nListening at ${port} port`);
    });
}