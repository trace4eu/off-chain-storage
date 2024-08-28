import CustomError from '../../exceptions/customError';

export class InvalidScopeException extends CustomError {
  constructor() {
    super();
    this.message = `[${this.constructor.name}] Invalid  scopes`;
  }
}
