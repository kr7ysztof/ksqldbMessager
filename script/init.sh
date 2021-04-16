docker-compose exec kafka kafka-topics --topic notification --create --bootstrap-server localhost:9092
docker-compose exec ksqldb-cli ksql --file /ksqlinit.sql http://primary-ksqldb-server:8088