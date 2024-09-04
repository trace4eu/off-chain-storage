import { HttpException, HttpStatus, Logger } from '@nestjs/common';
import { UnauthorizedException } from '../exceptions/unauthorized.exception';
import CustomError from '../exceptions/customError';
import InternalServerErrorException from '../exceptions/internalServerError.exception';
import BadRequestException from '../exceptions/badRequest.exception';
import NotFoundException from '../exceptions/notFound.exception';

const logger = new Logger();

export class ExceptionTypeFactory {
  static create(exception: Error) {
    if (
      exception instanceof HttpException &&
      exception.getStatus() === HttpStatus.UNAUTHORIZED
    ) {
      return new UnauthorizedException();
    }
    if (exception instanceof CustomError) {
      return exception as CustomError;
    }
    if (exception.name === 'BadRequestException') {
      return new BadRequestException(
        (exception as BadRequestException).getResponse().message,
      );
    }
    if (exception.name === 'NotFoundException') {
      return new NotFoundException(
        (exception as BadRequestException).getResponse().message,
      );
    }
    logger.error({
      method: `${ExceptionTypeFactory.name}.${this.name}`,
      message: 'Exception does not match any known type',
      customError: exception.stack,
    });
    return new InternalServerErrorException();
  }
}
