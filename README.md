### SSO Authorization Server API Documentation

* http://localhost:9000 (dev)
* https://domain (prod)
  
        /swagger-ui.html

---

## SSO Authorization Server (dev)

### Build SSO Server

docker build --file Dockerfile --tag sso:1.0 .

### Start SSO Server

docker stack deploy -c sso-dev.yml sso-dev

---

### Start SSO Server with NGINX Reverse Proxy

docker stack deploy -c sso-nginx-dev.yml sso-nginx-dev

---

## SSO Authorization Server (prod)

### Create Network

docker network create --driver overlay sso-net

### Create Volumes

docker volume create sso-volume

### Configure Mongo Cluster

requirement: 1 shard with 3 replicas without router and config servers

chmod 400 docker/mongo/keyfile.txt

docker stack deploy -c sso-db-mongo.yml sso-db-mongo

docker exec -i -t mongo_mongo-1.1.{id} /bin/bash

mongosh mongodb://{username}:{password}@localhost:27017/admin?authSource=admin

rs.initiate({ _id: 'shard-1', members: [
        { _id: 1, host: 'sso-db-mongo_mongo-1.1.{id}' },
        { _id: 2, host: 'sso-db-mongo_mongo-1.2.{id}' },
        { _id: 3, host: 'sso-db-mongo_mongo-1.3.{id}' }
]});

### Generate Certificates

mkdir -p ./docker/certbot/etc/letsencrypt

>   **Important** 
>  override  domain

docker compose up certbot

### Build SSO Server

docker build --file Dockerfile --tag sso:1.0 .

### Start SSO Server

>   **Important**
>  override  domain and certificate

docker stack deploy -c sso.yml sso

---

### Start SSO Server with NGINX Reverse Proxy

>   **Important**
>  override  domain  and  certificate

docker stack deploy -c sso-nginx.yml sso-nginx

---

### Renew Certificates

docker service scale {server_running_on_443_port}=0

docker compose up certbot-renew

docker service scale {server_running_on_443_port}={expected_replica_count}