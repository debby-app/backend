docker-compose down;
docker rmi Image debby_main:latest;
mvn clean install;
docker-compose up;