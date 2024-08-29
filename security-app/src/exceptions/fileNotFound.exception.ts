import CustomError from './customError';

export default class FileNotFoundException extends CustomError {
  constructor(message?: string) {
    super();
    this.message = `[${this.constructor.name}] File not found`;
  }
}
