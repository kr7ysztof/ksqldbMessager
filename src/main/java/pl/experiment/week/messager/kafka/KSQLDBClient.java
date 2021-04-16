package pl.experiment.week.messager.kafka;

import io.confluent.ksql.api.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class KSQLDBClient {

    private static final Logger logger =
            LoggerFactory.getLogger(KSQLDBClient.class);

    public Client getClient() {
        String KSQLDB_SERVER_HOST = "localhost";
        int KSQLDB_SERVER_HOST_PORT = 8088;
        ClientOptions options = ClientOptions.create()
                .setHost(KSQLDB_SERVER_HOST)
                .setPort(KSQLDB_SERVER_HOST_PORT);
        return Client.create(options);
    }

    public void acknowledge(Long id) throws ExecutionException, InterruptedException {

        Client client = getClient();

        StreamedQueryResult streamedQueryResult =
                client.streamQuery(
                        String.format("select * from notification_table_with_id " +
                                "where id=%d and (acknowledged = false or acknowledged is null) emit changes;", id)
                ).get();


        //For test blocking reader to get specific message id and acknowledge it -- if the is no message id in the table the blocking is forever :P
        Row row = streamedQueryResult.poll();
        if (row != null) {
            logger.info("Received notification!");
            logger.info("Row: " + row.values());

            KsqlObject acknowledge = new KsqlObject()
                    .put("id", row.getValue(1))
                    .put("origin", row.getValue(2))
                    .put("title", row.getValue(3))
                    .put("user", row.getValue(4))
                    .put("info", row.getValue(5))
                    .put("acknowledged", true);
            client.insertInto("notification_stream_id_with_key", acknowledge).get();
            logger.info("Acknowledged sent for id=" + row.getValue(1));
        } else {
            logger.info(String.format("No notification with id = %d", id));
        }
        client.close();
    }
}
