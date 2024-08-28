import CustomError from '../../exceptions/customError';

export class BearerTokenNotValidException extends CustomError {
  constructor(payload?: any) {
    super(undefined, payload);
    this.message = `[${this.constructor.name}] 'The token is not valid: ' ${payload.message}`;
  }
}
