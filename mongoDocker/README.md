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

kubectl apply -f  C:\Projects\CallMyOwnerSpring\kubernetes\back_deployment.yaml

delete deployment call-my-owner-back
deployment.apps "call-my-owner-back" deleted

1)
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\back_deployment.yaml
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\back_service.yaml
2)
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\front_deployment.yaml
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\front_service.yaml
3)
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\mongo_deployment.yaml
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\mongo_service.yaml
________________________
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\mongo_service.yaml
kubectl get ep call-my-owner-mongo                      //see which endpoints service manages
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\mongo_deployment.yaml
kubectl get pods --watch
NAME                                   READY   STATUS    RESTARTS   AGE
call-my-owner-mongo-7b5c6fdbdd-w46ng   1/1     Running   0          25s

kubectl exec -it call-my-owner-mongo-7b5c6fdbdd-w46ng bash
mongo
use admin
db.createUser(
   {
     user: "root",
     pwd: "password123",
     roles:
       [
         { role: "readWrite", 
           "role" : "root",
            "db" : "admin"},
       ]
   }
)
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\back_service.yaml
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\front_service.yaml
//get services ips and set them to back and front
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\back_deployment.yaml
kubectl apply -f C:\Projects\CallMyOwnerSpring\kubernetes\front_deployment.yaml
