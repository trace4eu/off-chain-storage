import CustomError from '../../exceptions/customError';

export class BadResponseFromAuthServiceException extends CustomError {
  constructor(payload?: any) {
    super(undefined, payload);
    this.message = `[${this.constructor.name}] 'There is no valid parameter in the response'`;
  }
}
