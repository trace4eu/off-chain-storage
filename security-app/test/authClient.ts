import { importJWK, JWK, SignJWT } from 'jose';

export class AuthClient {
  entityClientId = 'test-client-jwt';
  tokenUrl = 'https://api-dev-auth.trace4eu.eu/oauth2/token';
  entityJwk: JWK = {
    crv: 'P-256',
    d: 'TvKuAqV1y1pTlWhJ_BG9WZZvnu1n0bonuleoZDtT89k',
    kty: 'EC',
    x: '3qmoY1Bs0eJ319TLku5ofe7q2guicdFSIu22miBLXHY',
    y: 'gRzDzzvTGuyJp7ypFWuboC21KhsxpcpQMo9IcXSt23E',
    kid: 'test-client-jwt-jwk#1',
    use: 'sig',
  };
  requestTokenPayload = {
    grant_type: 'client_credentials',
    client_id: this.entityClientId,
    client_assertion_type:
      'urn:ietf:params:oauth:client-assertion-type:jwt-bearer',
    scope: 'ocs:write ocs:read',
  };
  async generateAssertion(): Promise<string> {
    const importedKey = await importJWK(this.entityJwk);
    const jws = new SignJWT(JSON.parse(payload.toString()))
      .setProtectedHeader({
        alg: alg || Algorithms.ES256K,
        typ: 'JWT',
      })
      .setExpirationTime(expiration)
      .sign(importedKey);
  }
  async getAccessToken(): Promise<string> {}
}
