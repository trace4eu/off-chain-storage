import {
  AuthServiceConnector,
  AuthServiceValidationResponse,
} from '../infrastructure/authServiceConnector';
import { PassportStrategy } from '@nestjs/passport';
import { Strategy } from 'passport-custom';
import { ExecutionContext, Injectable } from '@nestjs/common';
import { Request } from 'express';
import { AccessTokenNotValidException } from '../exceptions/accessTokenNotValid.exception';
import { AuthorizationHeaderException } from '../exceptions/authorizationHeader.exception';
import { BearerTokenException } from '../exceptions/bearerToken.exception';
import { BearerTokenNotValidException } from '../exceptions/bearerTokenNotValid.exception';
import { Reflector } from '@nestjs/core';
import { META_SCOPES } from '../decorators/scope.decorator';
import { UnauthorizedException } from '../../exceptions/unauthorized.exception';

export interface EntityData {
  scopes?: string[];
  sub?: string;
}

@Injectable()
export class Trace4euStrategy extends PassportStrategy(
  Strategy,
  'jwt-trace4eu',
) {
  constructor(private readonly authServiceConnector: AuthServiceConnector) {
    super();
  }

  async validate(req: Request): Promise<EntityData> {
    const authorizationHeader = this.getAuthorizationHeader(req);
    const bearerToken = this.getBearerToken(authorizationHeader);
    const responseIntrospect = await this.requestValidation(bearerToken);
    return this.injectClaims(responseIntrospect);
  }

  private injectClaims(
    responseIntrospect: AuthServiceValidationResponse,
  ): EntityData {
    const { scope, sub } = responseIntrospect;
    if (!scope || !sub) throw new AccessTokenNotValidException();
    return {
      scopes: scope.split(' '),
      sub,
    };
  }

  private getAuthorizationHeader(req: Request): string {
    const authorization = req.headers.authorization;
    if (!authorization) {
      throw new AuthorizationHeaderException({
        message: 'There is no authorization header',
        headers: req.headers,
        method: `${Trace4euStrategy.name}.${this.getAuthorizationHeader.name}`,
      });
    }
    return authorization;
  }

  private getBearerToken(authorizationHeader: string): string {
    const [name, bearerToken] = authorizationHeader.split(' ');
    if (name != 'Bearer' || !bearerToken || bearerToken.trim() === '') {
      throw new BearerTokenException({
        message: 'There is no bearer token in the authorization header',
        authorizationHeader: authorizationHeader,
        method: `${Trace4euStrategy.name}.${this.getBearerToken.name}`,
      });
    }
    return bearerToken;
  }

  private async requestValidation(
    bearerToken: string,
  ): Promise<AuthServiceValidationResponse> {
    const data = await this.authServiceConnector.requestValidation(bearerToken);
    if (!data.active) {
      throw new BearerTokenNotValidException();
    }
    return data;
  }
}
