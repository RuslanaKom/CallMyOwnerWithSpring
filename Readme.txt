C:\Projects\CallMyOwnerSpring>http-server ./

DOCKER:
login:
docker login -u=ruslana -p=passwordd
build:
>> docker build -f Dockerfile -t ruslana/call_my_owner .

push to docker repo:
>> docker push ruslana/call_my_owner

docker pull ruslana/call_my_owner
docker run -d -p 9999:9999 --name call-my-owner-back ruslana/call_my_owner


____
docker ps
docker ps -a
docker images
docker logs containerID
