import CustomError from './customError';

export default class InternalServerErrorException extends CustomError {
  constructor(message?: string) {
    super();
    this.message = `[${this.constructor.name}] ${message}`;
  }
}
