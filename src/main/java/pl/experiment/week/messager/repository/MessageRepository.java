package pl.experiment.week.messager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.experiment.week.messager.model.Message;

import java.util.Collection;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("FROM Message m WHERE m.acknowledged = false and m.user = ?1")
    Collection<Message> findAllNotAcknowledgedByUser(String user);
}
