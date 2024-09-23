import { Test } from '@nestjs/testing';
import { INestApplication } from '@nestjs/common';
import * as request from 'supertest';
import { AppModule } from '../src/app.module';

describe('FilesPrivateController (e2e) should', () => {
  let application: INestApplication;

  beforeEach(async () => {
    const module = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();
    application = module.createNestApplication();
    await application.init();
  });

  it('return 404 when no path matches the ones configured in the application', () => {
    return request(application.getHttpServer()).get('/').expect(404);
  });

  it('allow to create a private file', () => {
    return request(application.getHttpServer())
      .post('/files')
      .set('Authorization', 'Bearer ' + fakeSuperAdminToken())
      .send(createEntityDto);
  });
});
