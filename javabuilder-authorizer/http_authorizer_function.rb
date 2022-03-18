require 'aws-sdk-lambda'
require 'jwt'
require_relative 'jwt_helper'
include JwtHelper

# The lambda handler takes an event with the query string parameter
# 'Authorization=token',  where the token is a JWT token generated
# by dashboard. The handler verifies the token with the appropriate
# public key (dependant on the environment the request came from),
# and checks the token has not expired and its issue time is not in the future.
def lambda_handler(event:, context:)
  origin = event['headers']['origin']
  jwt_token = event['queryStringParameters']['Authorization']
  route_arn = event['routeArn']

  if jwt_token
    begin
      decoded_token = JWT.decode(
        jwt_token,
        # Temporarily choose the key based on the client origin rather than the
        # route_arn until we have environment-specific Javabuilders set up.
        # get_public_key(route_arn),
        JwtHelper.get_public_key(origin),
        true,
        verify_iat: true, # verify issued at time is valid
        algorithm: 'RS256'
      )
    rescue JWT::ExpiredSignature, JWT::InvalidIatError
      return JwtHelper.generate_deny(nil, route_arn)
    end

    return JwtHelper.generate_deny(nil, route_arn) unless decoded_token

    token_payload = decoded_token[0]
    user_id = token_payload['uid']
    issuer = token_payload['iss']
    principal_id = "#{issuer}/#{user_id}"

    JwtHelper.generate_allow(principal_id, route_arn, token_payload)
  else
    JwtHelper.generate_deny(nil, route_arn)
  end
end
