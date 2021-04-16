# Kafka spring and ksqldb example

## 1. start dev environment
```
docker-compose up -d
```
## 2. run init script

```aidl
./script/init.sh
```

## 3. start the app

## 4. put a message in the kafka topic
```
docker-compose exec kafka /bin/bash
#then in the container:
kafka-console-producer --broker-list localhost:9092 --topic notification 
#and insert:
{"origin":"app1","title":"I want to notify you","user":"user123","info":"INFO", "time":1618395298}
```

## 5. acknowledge message in db

```
POST http://localhost:8080/message/db/1/acknowledge
```

## 6. acknowledge message in ksqldb

```
POST http://localhost:8080/message/ksqldb/1/acknowledge
```



