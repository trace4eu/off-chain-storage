import CustomError from './customError';

export default class ForbiddenException extends CustomError {
  constructor() {
    super();
    this.message = `[${this.constructor.name}]`;
  }
}
