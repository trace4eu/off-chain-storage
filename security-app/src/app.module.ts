import { Module } from '@nestjs/common';
import { AppController } from './controllers/app.controller';
import { ApiConfigModule } from '../config/configuration';
import { HttpModule } from '@nestjs/axios';
import { AuthModule } from './auth/auth.module';

@Module({
  imports: [AuthModule, ApiConfigModule, HttpModule],
  controllers: [AppController],
  providers: [],
})
export class AppModule {}
