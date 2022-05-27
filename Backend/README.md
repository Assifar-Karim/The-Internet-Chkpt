# The Internet Checkpoint Backend

## Docker build command:

To build the backend server using docker user the following command

> `docker build -t ticp_back .`

## Docker run command:

To start the docker container, you must have the following environment variables set up in your host machine:
- MONGO_DB_USERNAME
- MONGO_DB_PASSWORD
- ENVIRONMENT
- GOOGLE_OAUTH2_CLIENT_ID
- GOOGLE_CLIENT_SECRET
- MAIL_PASSWORD
- JWT_SECRET_KEY

>`docker run -p 8080:8080 --name ticp_back -e MONGO_DB_USERNAME=$MONGO_DB_USERNAME -e MONGO_DB_PASSWORD=$MONGO_DB_PASSWORD -e ENVIRONMENT=$ENVIRONMENT -e GOOGLE_OAUTH2_CLIENT_ID=$GOOGLE_OAUTH2_CLIENT_ID -e GOOGLE_CLIENT_SECRET=$GOOGLE_CLIENT_SECRET -e MAIL_PASSWORD=$MAIL_PASSWORD -e JWT_SECRET_KEY=$JWT_SECRET_KEY ticp_back`