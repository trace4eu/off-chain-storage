import {
  CallHandler,
  ExecutionContext,
  Injectable,
  NestInterceptor,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { META_SCOPES, ValidScopes } from '../decorators/scope.decorator';
import { InvalidScopeException } from '../exceptions/InvalidScope.exception';

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
    const requiredScopes: ValidScopes[] = Reflect.getMetadata(
      META_SCOPES,
      context.getHandler(),
    );

    const req = context.switchToHttp().getRequest();
    const { scopes } = req.user;

    for (const scope of scopes) {
      if (requiredScopes.includes(scope)) {
        return next.handle();
      }
    }

    throw new InvalidScopeException();
  }
}
