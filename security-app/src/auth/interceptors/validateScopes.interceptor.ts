import {
  CallHandler,
  ExecutionContext,
  Injectable,
  NestInterceptor,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { META_SCOPES } from '../decorators/scope.decorator';

@Injectable()
export class ValidateScopesInterceptor implements NestInterceptor {
  constructor() {}
  intercept(
    context: ExecutionContext,
    next: CallHandler<any>,
  ): Observable<any> | Promise<Observable<any>> {
    const keys = Reflect.getMetadataKeys(context.getHandler());
    if (!keys.find((key) => key === META_SCOPES)) {
      return next.handle();
    }
    const requiredScopes = Reflect.getMetadata(
      META_SCOPES,
      context.getHandler(),
    );

    const req = context.switchToHttp().getRequest();
    const { scopes } = req.user;
    return next.handle();
    // return throwError(() => new NotFoundException('Endpoint not found'));
  }
}
