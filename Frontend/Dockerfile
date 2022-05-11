# Stage 0
FROM node:14-slim AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY ./src ./src
COPY ./public ./public

RUN npm run build

# Stage 1
FROM nginx:alpine

COPY --from=build /app/build/ /usr/share/nginx/html
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]