import { Inject, Injectable } from '@nestjs/common';
import axios from 'axios';
import { AuthServiceValidationException } from '../exceptions/authServiceValidation.exception';
import { ConfigService } from '@nestjs/config';
import { ApiConfig } from '../../../config/configuration';

export interface AuthServiceValidationResponse {
  active: boolean;
  scope?: string;
  client_id?: string;
  sub?: string;
  exp?: number;
  iat?: number;
  nbf?: number;
  aud?: string[];
  iss?: string;
  token_type?: string;
  token_use?: string;
}

@Injectable()
export class AuthServiceConnector {
  private readonly authServiceUrl;
  constructor(private configService: ConfigService<ApiConfig, true>) {
    this.authServiceUrl = this.configService.get<string>(
      'tokenIntrospectionUrl',
    );
  }

  async requestValidation(
    bearerToken: string,
  ): Promise<AuthServiceValidationResponse> {
    const headers = {
      Authorization: `Bearer ${this.configService.get<string>('adminBearerToken')}`,
      'Content-Type': 'application/x-www-form-urlencoded',
    };
    try {
      const response = await axios.post(
        `${this.authServiceUrl}`,
        new URLSearchParams({ token: bearerToken }).toString(),
        {
          headers,
        },
      );
      return response.data;
    } catch (error) {
      throw new AuthServiceValidationException(error, { bearerToken });
    }
  }
}
