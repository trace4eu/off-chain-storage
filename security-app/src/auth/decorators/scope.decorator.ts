import { applyDecorators, SetMetadata, UseInterceptors } from '@nestjs/common';
import { ValidateScopesInterceptor } from '../interceptors/validateScopes.interceptor';
export enum ValidScopes {
  ocsRead = 'ocs:read',
  ocsWrite = 'ocs:write',
}

export const META_SCOPES = 'scopes';

export const ScopesProtected = (scopes: ValidScopes[]) =>
  applyDecorators(
    UseInterceptors(ValidateScopesInterceptor),
    SetMetadata(META_SCOPES, scopes),
  );
