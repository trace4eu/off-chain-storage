import CustomError from '../../exceptions/customError';

export class AccessTokenNotValidException extends CustomError {
  constructor() {
    super(undefined);
    this.message = `[${this.constructor.name}] Scope and sub claims are required in the access token`;
  }
}
