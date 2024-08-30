import { Module } from '@nestjs/common';
import { FilesPrivateController } from './controllers/files.private.controller';
import { ApiConfigModule } from '../config/configuration';
import { AuthModule } from './auth/auth.module';
import AppService from './services/app.service';
import { FilesPublicController } from './controllers/files.public.controller';

@Module({
  imports: [AuthModule, ApiConfigModule],
  controllers: [FilesPrivateController, FilesPublicController],
  providers: [AppService],
})
export class AppModule {}
