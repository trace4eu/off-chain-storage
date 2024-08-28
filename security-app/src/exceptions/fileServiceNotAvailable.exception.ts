import CustomError from './customError';

export default class FileServiceNotAvailableException extends CustomError {
  constructor() {
    super();
    this.message = `[${this.constructor.name}] FileService app not available`;
  }
}
