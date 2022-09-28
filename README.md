# The Internet Checkpoint
<p align="center" >
<img src= "https://raw.githubusercontent.com/Assifar-Karim/The-Internet-Chkpt/main/Res/icons/Internet-Checkpoint-logo.gif" height="240" width="auto" />
</p>

## Description

The Internet Checkpoint is a project which aims to be an homage to the initial Internet Checkpoint that could initially be found on the now deleted Youtube channel TAIA777.

## Demo Production Environment

A demonstration of The Internet Checkpoint can be found [here](https://frontend-service-assifar-karim.cloud.okteto.net).


## Installation

The first step towards installing this project on a machine is to clone it using the following command:

```bash
git clone git@github.com:Assifar-Karim/The-Internet-Chkpt.git
```
 
> NOTE: For the application to fully work, locally, modify the application.yml file's database link to a link to a database instance you own either on Mongo Atlas or locally and setup your own google oauth2 configuration and email address 

### Local Installation
- Frontend: Go to the Frontend directory and run:  `npm install`
- Backend: Go to the Backend directory and run: `./mvnw compile`

### Docker Installation
- Frontend: Go to the Frontend directory and run: `docker build -t ticp-front .`
- Backend: Go to the Backend directory and run: `docker build -t ticp-back .`

## Environment Variables

To run this project, you will need to add the following environment variables to your machine/containers or development environment:

- **MONGO_DB_USERNAME**
- **MONGO_DB_PASSWORD**
- **JWT_SECRET_KEY**
- **MAIL_PASSWORD**
- **GOOGLE_CLIENT_SECRET**
- **GOOGLE_OAUTH2_CLIENT_ID**
- **ENVIRONMENT**
- **REDIS_HOST**
- **REDIS_PORT**
- **CORS_ALLOWED_ORIGIN**
- **SERVER_LOCATION**

## How To Use?

After all the of the preliminary setup is done you can start using hosting the app by following one these 3 methods:

> NOTE: For the backend to work a Redis instance needs to be running on your machine. You can either install it locally or through a docker container.

### Run It Locally

> NOTE: Running the app locally will be through dev mode and the resulting output is not production based

- Backend: Run the command `./mvnw spring-boot:run`
- Frontend: Run the command `npm start`

### Run It With Docker

- Backend: Run the following command
```bash
docker run -p 8080:8080 --name ticp-backend \
-e MONGO_DB_USERNAME=$MONGO_DB_USERNAME \
-e MONGO_DB_PASSWORD=$MONGO_DB_PASSWORD \
-e JWT_SECRET_KEY=$JWT_SECRET_KEY \
-e MAIL_PASSWORD=$MAIL_PASSWORD \
-e GOOGLE_CLIENT_SECRET=$GOOGLE_CLIENT_SECRET \
-e GOOGLE_OAUTH2_CLIENT_ID=$GOOGLE_OAUTH2_CLIENT_ID \
-e ENVIRONMENT=$ENVIRONMENT \
-e REDIS_HOST=$REDIS_HOST \
-e REDIS_PORT=$REDIS_PORT \
-e CORS_ALLOWED_ORIGIN=$CORS_ALLOWED_ORIGIN \
ticp-back
```

- Frontend: Run the following command 

```bash
docker run -p 80:80 -e SERVER_LOCATION=$SERVER_LOCATION --name ticp-frontend ticp-front
```

> Recommendation: You can also decide to run the app using docker compose by using the **`docker compose up`** command.


### Run It With Kubernetes
Start by creating a secrets manifest file, called *ticp-secret.yml* that contains the secrets that are referenced by the backend manifest. Then run the following command:

```bash
kubectl apply -f k8s
```

## Tech Stack
- **Client Side**: *ReactJS*
- **Server Side**: *Spring Boot* | *Nginx* | *MongoDB* | *Redis* 
- **QA**: *JUnit 5* | *Mockito* | *Spring Boot Tests*
- **Deployment tools**: *Docker* | *Kubernetes*
- **CI/CD**: *Circle CI*

<pre align="center">
THE INTERNET CHECKPOINT | TICP TEAM 2022
<img src="https://raw.githubusercontent.com/Assifar-Karim/The-Internet-Chkpt/main/Res/icons/dark-souls-bonfire.gif"/>
</pre>