import { ConfigModule } from '@nestjs/config';
import * as Joi from 'joi';

export interface ApiConfig {
  apiPort: number;
  apiUrlPrefix: string | undefined;
  cassandraAppUrl: string | undefined;
  tokenIntrospectionUrl: string | undefined;
  adminBearerToken: string | undefined;
}

export const loadConfig = (): ApiConfig => {
  return {
    apiPort: parseInt(process.env.API_PORT || '3000', 10),
    apiUrlPrefix: process.env.API_URL_PREFIX,
    cassandraAppUrl: process.env.CASSANDRA_APP_URL,
    tokenIntrospectionUrl: process.env.TOKEN_INTROSPECTION_URL,
    adminBearerToken: process.env.ADMIN_BEARER_TOKEN,
  };
};

export const ApiConfigModule = ConfigModule.forRoot({
  envFilePath: [
    `.env.${process.env.NODE_ENV}.local`,
    `.env.${process.env.NODE_ENV}`,
    '.env.local',
    '.env',
  ],
  load: [loadConfig],
  validationSchema: Joi.object({
    API_PORT: Joi.string().default('3000'),
    API_URL_PREFIX: Joi.string(),
    CASSANDRA_APP_URL: Joi.string().required(),
    TOKEN_INTROSPECTION_URL: Joi.string().required(),
    ADMIN_BEARER_TOKEN: Joi.string().required(),
  }),
});
