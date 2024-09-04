import CustomError from './customError';

export default class CassandraAppException extends CustomError {
  constructor() {
    super();
    this.message = `[${this.constructor.name}] Cassandra app not available`;
  }
}
