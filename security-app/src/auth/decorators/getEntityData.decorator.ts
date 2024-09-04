import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import InternalServerErrorException from '../../exceptions/internalServerError.exception';
import ForbiddenException from '../../exceptions/forbidden.exception';

export const GetEntityData = createParamDecorator(
  (data: string, ctx: ExecutionContext) => {
    const req = ctx.switchToHttp().getRequest();
    const { scopes, sub } = req.user;

    if (!scopes || !sub) throw new ForbiddenException();

    return {
      scopes,
      sub,
    };
  },
);
