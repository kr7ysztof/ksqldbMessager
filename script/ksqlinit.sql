SET 'auto.offset.reset'='earliest';

CREATE
STREAM notification_src (
     time BIGINT,
     origin VARCHAR,
     title VARCHAR,
     user VARCHAR,
	 info VARCHAR,
	 acknowledged BOOLEAN
   ) WITH (
     KAFKA_TOPIC = 'notification',
     VALUE_FORMAT = 'JSON'
   );

-- generate autoincremet id
CREATE TABLE notification_id
    WITH (partitions = 1) AS
SELECT 1                        as k,
       LATEST_BY_OFFSET(origin) as origin,
       LATEST_BY_OFFSET(title)  as title,
       LATEST_BY_OFFSET(user) as user,
	  LATEST_BY_OFFSET(info) as info,
	  LATEST_BY_OFFSET(acknowledged) as acknowledged,
	  LATEST_BY_OFFSET(time) as time,
      COUNT(1) AS id
FROM notification_src
GROUP BY 1;

CREATE
STREAM notification_stream_with_id (
    id BIGINT,
    origin VARCHAR,
    title VARCHAR,
    user VARCHAR,
	info VARCHAR,
	acknowledged BOOLEAN
  ) WITH (
    KAFKA_TOPIC = 'NOTIFICATION_ID',
    VALUE_FORMAT = 'JSON'
  );

create
stream notification_stream_id_with_key as
select *
from notification_stream_with_id partition by id;


-- create notification table with id
CREATE TABLE notification_table_with_id
(
    id           BIGINT PRIMARY KEY,
    origin       VARCHAR,
    title        VARCHAR,
    user         VARCHAR,
    info         VARCHAR,
    acknowledged BOOLEAN
) WITH (
      KAFKA_TOPIC = 'NOTIFICATION_STREAM_ID_WITH_KEY',
      VALUE_FORMAT = 'JSON'
      );