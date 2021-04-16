package pl.experiment.week.messager.kafka;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Controller;
import pl.experiment.week.messager.model.Message;
import pl.experiment.week.messager.repository.MessageRepository;

@Controller
public class KafkaConsumer {

    private static final Logger logger =
            LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private MessageRepository messageRepository;

    @Bean
    public ConsumerFactory<String, Message> consumerFactory() {
        final JsonDeserializer<Message> jsonDeserializer = new JsonDeserializer<>(Message.class);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @KafkaListener(topics = "notification", containerFactory = "kafkaListenerContainerFactory")
    public void listen(Message message) {

        logger.info("Logger Kafka [JSON]  {}", message);
        messageRepository.save(message);
    }
}
