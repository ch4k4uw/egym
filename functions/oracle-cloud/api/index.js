const port = 3000;

const admin = require('firebase-admin');
const express = require('express');
const Fs = require("fs-extra");
const https = require("https");
var serviceAccount = require("./e-gym-6ca5d-firebase-adminsdk-4wcfh-efc6b15dcb.json");

admin.initializeApp({ credential: admin.credential.cert(serviceAccount) });
const app = express();

app.get('/:collection/count', async (req, res) => {
    const collectionName = req.params.collection;
    try {
        console.log(admin);
        const count = (await admin.firestore()
            .collection(collectionName)
            .select(admin.firestore.FieldPath.documentId())
            .get()).docs.length;
        res.status(200).send({count});
    } catch (e) {
        console.error(e);
        res.status(500).send({ message: 'internal server error', cause: e || 'unknown' });
    }
});

if (process.env.NODE_ENV === 'production') {
    const key = Fs.readFileSync('/certs/privkey1.pem');
    const cert = Fs.readFileSync('/certs/fullchain1.pem');
    https.createServer({ key, cert }, app).listen(port, () => {
        console.log(`Listening at ${port} security port`);
    });
}
else {
    app.listen(port, () => {
        console.log(`Listening at ${port} port`);
    });
}