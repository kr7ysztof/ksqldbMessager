package pl.experiment.week.messager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.experiment.week.messager.kafka.KSQLDBClient;
import pl.experiment.week.messager.model.Message;
import pl.experiment.week.messager.repository.MessageRepository;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/message")
public class MessageController {

    private static final Logger logger =
            LoggerFactory.getLogger(MessageController.class);

    private final MessageRepository messageRepository;
    private final KSQLDBClient ksqldbClient;

    public MessageController(MessageRepository messageRepository, KSQLDBClient ksqldbClient) {
        this.messageRepository = messageRepository;
        this.ksqldbClient = ksqldbClient;
    }

    @PostMapping(path = "/db/{id}/acknowledge")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void acknowledgeDB(@PathVariable Long id) {
        try {
            Message message = messageRepository.findById(id).orElseThrow();
            message.setAcknowledged(true);
            messageRepository.save(message);
            logger.info("Logger Kafka [JSON] message acknowledged {}", message);
        } catch (NoSuchElementException exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Message Not Found");
        }
    }

    @PostMapping(path = "/ksqldb/{id}/acknowledge")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void acknowledgeKSQLDB(@PathVariable Long id) {
        try {
            ksqldbClient.acknowledge(id);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Message Not Found");
        }
    }
}
