import CustomError from './customError';

export default class InternalServerErrorException extends CustomError {
  constructor() {
    super();
    this.message = `[${this.constructor.name}]`;
  }
}
