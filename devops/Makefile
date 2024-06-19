build-image:
	@ docker buildx build -f Dockerfile -t trackit:1 .
volumen:
	@ docker volume create mysql_data
network:
	@ docker network create --driver overlay --scope swarm desarrollo_net || true
deploy:
	@ docker stack deploy --with-registry-auth -c stack.yml trackit
rm:
	@ docker stack rm trackit