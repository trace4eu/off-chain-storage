import { Module } from '@nestjs/common';
import { AppController } from './controllers/app.controller';
import { ApiConfigModule } from '../config/configuration';
import { AuthModule } from './auth/auth.module';
import AppService from './services/app.service';

@Module({
  imports: [AuthModule, ApiConfigModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
