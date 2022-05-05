#!/bin/bash

# Runs integration tests in standalone mode locally. The commands here are mostly the same as those executed in the integration testing step
# in the Javabuilder code pipeline.
#
# NOTE: This script assumes you have command line access to the production Code.org AWS account. Check the README for details.

set -e

. standalone.config

npm install

# TODO: Use integration tests specific private key and password once those have been added.
private_key=$(aws secretsmanager get-secret-value --secret-id arn:aws:secretsmanager:us-east-1:475661607190:secret:development/cdo/javabuilder_private_key-gZE3SO)
password=$(aws secretsmanager get-secret-value --secret-id arn:aws:secretsmanager:us-east-1:475661607190:secret:development/cdo/javabuilder_key_password-J1RILi)

JAVABUILDER_PRIVATE_KEY=$private_key \
JAVABUILDER_PASSWORD=$password \
JAVABUILDER_HTTP_URL=https://"$sub_domain"-http."$base_domain"/seedsources/sources.json \
JAVABUILDER_WEBSOCKET_URL=wss://"$sub_domain"."$base_domain" \
npm test
