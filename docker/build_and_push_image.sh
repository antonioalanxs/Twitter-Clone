cd ..
docker build -t $1 -f docker/Dockerfile .
cd ./docker
docker push $1
