docker network create my-network --driver bridge

docker network ls

WORKING for  bitnami image:
docker run -d -p 27018:27017 --name my-mongodb-server -e MONGODB_ROOT_PASSWORD=password123 -v C:\Users\ruslana.komaristova\MyMongoPersistance:/bitnami/monodb bitnami/mongodb:latest

docker exec -it my-mongodb-server  bash

mongo -u root -p password123

use lostStuffDb  (creates bew DB)
db.createCollection('users')
db.createCollection('stuff')


_____________________________________kubernetes
kubectl create -f front_pod.yaml
kubectl create -f front_service.yaml

kubectl apply -f deployment.yaml



kubectl apply -f  C:\Projects\CallMyOwnerSpring\kubernetes\back_deployment.yaml

delete deployment call-my-owner-back
deployment.apps "call-my-owner-back" deleted
