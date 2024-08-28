import CustomError from '../../exceptions/customError';

export class AuthServiceValidationException extends CustomError {
  constructor(error?: Error, payload?: object) {
    super(error, payload);
    this.message = `[${this.constructor.name}] 'Error calling auth service validation'`;
  }
}
