import CustomError from './customError';

export class UnauthorizedException extends CustomError {
  constructor(message?: string) {
    super();
    this.message = `[${this.constructor.name}] ${message}`;
  }
}
