build-image:
	@ docker build -f Dockerfile -t traclit:1 .
volumen:
	@ docker volume create pg_tendencia_instituto_data
network:
	@ docker network create --drive overlay --scope swarm desarrollo_net || true
deploy:
	@ docker stack deploy --with-registry-auth -c stack.yml prueba
rm:
	@ docker stack rm prueba