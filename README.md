## SSO Authorization Server

### Create Network

sudo docker network create --driver overlay sso-net

### Create Volumes

sudo docker volume create sso-volume

### Configure Mongo Cluster

requirement: 1 shard with 3 replicas without router and config servers

sudo chown 999:999 docker/mongo/keyfile.txt && sudo chmod 400 docker/mongo/keyfile.txt

sudo docker stack deploy -c mongo.yml mongo

sudo docker exec -i -t mongo_mongo-1.1.{id} /bin/bash

mongosh mongodb://{username}:{password}@localhost:27017/admin?authSource=admin

rs.initiate({ _id: 'shard-1', members: [
        { _id: 1, host: 'mongo_mongo-1.1.{id}' },
        { _id: 2, host: 'mongo_mongo-1.2.{id}' },
        { _id: 3, host: 'mongo_mongo-1.3.{id}' }
]});

### Generate Certificates

mkdir -p ./docker/certbot/etc/letsencrypt

>   **Important** 
>  override  domain

sudo docker compose up certbot

### Build SSO Server

sudo docker build --file Dockerfile --tag sso:1.0 .

### Start SSO Server

>   **Important**
>  override  domain and certificate

sudo docker stack deploy -c sso.yml sso

### Start SSO Server with NGINX Reverse Proxy

>   **Important**
>  override  domain  and  certificate

sudo docker stack deploy -c sso-nginx.yml sso-nginx

### SSO Server API Documentation

    /swagger-ui/index.html

### Renew Certificates

sudo docker service scale {server_running_on_443_port}=0

sudo docker compose up certbot-renew

sudo docker service scale {server_running_on_443_port}={expected_replica_count}