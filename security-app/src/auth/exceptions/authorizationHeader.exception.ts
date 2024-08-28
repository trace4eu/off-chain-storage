import CustomError from '../../exceptions/customError';

export class AuthorizationHeaderException extends CustomError {
  constructor(payload?: any) {
    super(undefined, payload);
    this.message = `[${this.constructor.name}] 'There is no authorization header'`;
  }
}
