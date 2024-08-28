import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import InternalServerErrorException from '../../exceptions/internalServerError.exception';

export const GetEntityData = createParamDecorator(
  (data: string, ctx: ExecutionContext) => {
    const req = ctx.switchToHttp().getRequest();
    const { scopes, sub } = req.user;

    if (!scopes || !sub)
      throw new InternalServerErrorException('Entity data not found');

    return {
      scopes,
      sub,
    };
  },
);
