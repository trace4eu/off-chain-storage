import CustomError from './customError';

export default class BadRequestException extends CustomError {
  constructor(errorMessage: string) {
    super();
    this.message = `[${this.constructor.name}] ${errorMessage}`;
  }
}
