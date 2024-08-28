import CustomError from '../../exceptions/customError';

export class BearerTokenNotValidException extends CustomError {
  constructor() {
    super(undefined);
    this.message = `[${this.constructor.name}] The token is not valid`;
  }
}
