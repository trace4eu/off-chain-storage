import { NestFactory } from '@nestjs/core';
import { ConfigService } from '@nestjs/config';
import { AppModule } from './app.module';
import type { ApiConfig } from '../config/configuration';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';
import { ValidationPipe } from '@nestjs/common';
import AllExceptionsFilter from './filters/allExceptions.filter';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.useGlobalPipes(new ValidationPipe());
  app.useGlobalFilters(new AllExceptionsFilter());

  const configService = app.get<ConfigService<ApiConfig, true>>(ConfigService);
  const apiUrlPrefix = configService.get<string>('apiUrlPrefix');
  const port = configService.get<number>('apiPort');

  app.setGlobalPrefix(apiUrlPrefix);

  const config = new DocumentBuilder()
    .setTitle('Trace4EU - off-chain storage')
    .setVersion('0.1')
    .setDescription('Off-chain storage api documentation')
    .addBearerAuth()
    .build();
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup(`${apiUrlPrefix}/api-docs`, app, document);

  await app.listen(port);
}
bootstrap();
