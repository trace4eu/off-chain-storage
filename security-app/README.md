# OCS-SECURITY-APP

Application developed using [Nest](https://github.com/nestjs/nest) framework TypeScript.

## Installation

```bash
$ npm install
```

## Running the app

```bash
# development
$ npm run start

# watch mode
$ npm run start:dev

# production mode
$ npm run start:prod
```

## Running the app with docker
Build docker container:
```bash
docker build --build-arg API_PORT=3000 -t trace4eu-ocs .
```
Run container with env variables:
```bash
docker run --env-file .env -d -p 3000:3000 --name trace4eu-ocs trace4eu-ocs:latest
```

## Test

```bash
# unit tests
$ npm run test

# e2e tests
$ npm run test:e2e

# test coverage
$ npm run test:cov
```
