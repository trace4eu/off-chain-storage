import { HttpStatus } from '@nestjs/common';
import CassandraAppException from '../exceptions/cassandraApp.exception';
import { InvalidScopeException } from '../auth/exceptions/InvalidScope.exception';
import { AuthorizationHeaderException } from '../auth/exceptions/authorizationHeader.exception';
import { AuthServiceValidationException } from '../auth/exceptions/authServiceValidation.exception';
import { BadResponseFromAuthServiceException } from '../auth/exceptions/badResponseFromAuthService.exception';
import { BearerTokenException } from '../auth/exceptions/bearerToken.exception';
import { BearerTokenNotValidException } from '../auth/exceptions/bearerTokenNotValid.exception';
import FileNotFoundException from '../exceptions/fileNotFound.exception';
import ForbiddenException from '../exceptions/forbidden.exception';

const ExceptionsHttpCodes = {
  [CassandraAppException.name]: HttpStatus.INTERNAL_SERVER_ERROR,
  [InvalidScopeException.name]: HttpStatus.FORBIDDEN,
  [AuthorizationHeaderException.name]: HttpStatus.UNAUTHORIZED,
  [AuthServiceValidationException.name]: HttpStatus.UNAUTHORIZED,
  [BadResponseFromAuthServiceException.name]: HttpStatus.INTERNAL_SERVER_ERROR,
  [BearerTokenException.name]: HttpStatus.UNAUTHORIZED,
  [BearerTokenNotValidException.name]: HttpStatus.UNAUTHORIZED,
  [FileNotFoundException.name]: HttpStatus.BAD_REQUEST,
  [ForbiddenException.name]: HttpStatus.FORBIDDEN,
  unknown: HttpStatus.INTERNAL_SERVER_ERROR,
};

export function getExceptionHttpCode(exceptionName: string): number {
  return ExceptionsHttpCodes[exceptionName] || ExceptionsHttpCodes.unknown;
}
