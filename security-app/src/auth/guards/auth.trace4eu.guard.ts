import { AuthGuard } from '@nestjs/passport';
import { ExecutionContext, Injectable } from '@nestjs/common';
import { UnauthorizedException } from '../../exceptions/unauthorized.exception';
@Injectable()
export class JwtAuthGuardTrace4eu extends AuthGuard('jwt-trace4eu') {
  constructor() {
    super();
  }
  canActivate(context: ExecutionContext) {
    return super.canActivate(context);
  }

  handleRequest(err: Error, response: any) {
    if (err || !response) {
      throw err || new UnauthorizedException();
    }
    return response;
  }
}
