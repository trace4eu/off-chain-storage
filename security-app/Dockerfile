ARG API_PORT

# Stage 1 - building node_modules and dist folder
FROM node:20.13.1-alpine3.20 as builder
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Stage 2
FROM node:20.13.1-alpine3.20
WORKDIR /usr/src/app
COPY --from=builder node_modules node_modules
COPY --from=builder dist dist
COPY package*.json ./
RUN npm prune --production
USER node
ENV NODE_ENV production
EXPOSE $API_PORT
CMD [ "node", "dist/src/main.js" ]
