#!/bin/bash

# Run mock-oauth2-server using Docker
# This will provide a local OIDC server at http://localhost:8081
# The discovery endpoint will be http://localhost:8081/default/.well-known/openid-configuration

docker run -d --name mock-oidc-server -p 8081:8081 -e SERVER_PORT=8081 ghcr.io/navikt/mock-oauth2-server:2.1.10

echo "Mock OIDC Server started at http://localhost:8081"
echo "Interactive UI available at http://localhost:8081/default/debugger"
echo "Discovery endpoint: http://localhost:8081/default/.well-known/openid-configuration"
