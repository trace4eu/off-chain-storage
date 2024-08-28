import CustomError from './customError';

export default class CassandraAppException extends CustomError {
  constructor(message?: string) {
    super();
    this.message = `[${this.constructor.name}] Cassandra app error ${message}`;
  }
}
