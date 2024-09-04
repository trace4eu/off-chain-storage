import { Test } from '@nestjs/testing';
import { INestApplication } from '@nestjs/common';
import * as request from 'supertest';
import { AppModule } from '../src/app.module';

describe('FilesPrivateController (e2e)', () => {
  let application: INestApplication;

  beforeEach(async () => {
    const module = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();
    application = module.createNestApplication();
    await application.init();
  });

  it('/ (GET)', () => {
    return request(application.getHttpServer()).get('/').expect(404);
  });
});
