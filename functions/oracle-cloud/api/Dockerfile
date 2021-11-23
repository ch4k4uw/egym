FROM node:12

ENV NODE_ENV=production

# Create app directory
WORKDIR /usr/src/app

COPY package*.json ./

RUN npm config set loglevel warn \
    && npm config set only production \
	&& npm config set progress false \
    && npm ci --only=production

COPY . .

EXPOSE 8080

CMD [ "node", "index.js" ]
