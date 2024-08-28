import { Module } from '@nestjs/common';
import { AuthServiceConnector } from './infrastructure/authServiceConnector';
import { ConfigModule } from '@nestjs/config';
import { PassportModule } from '@nestjs/passport';
import { Trace4euStrategy } from './strategies/auth.trace4eu.strategy';

@Module({
  imports: [ConfigModule, PassportModule],
  providers: [Trace4euStrategy, AuthServiceConnector],
  exports: [],
})
export class AuthModule {}
