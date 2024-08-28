import CustomError from '../../exceptions/customError';

export class BearerTokenException extends CustomError {
  constructor(payload?: any) {
    super(undefined, payload);
    this.message = `[${this.constructor.name}] 'There is no bearer token in the authorization header'`;
  }
}
