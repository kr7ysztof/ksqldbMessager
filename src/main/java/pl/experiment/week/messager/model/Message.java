package pl.experiment.week.messager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;

@ToString
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message implements Serializable {

    private @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    private @NonNull long time;
    private @NonNull String origin;
    private String title;
    private @NonNull String user;
    private String level;
    private boolean acknowledged;

    public Message(@JsonProperty("time")final long time,
                   @JsonProperty("origin") final String origin,
                   @JsonProperty("title") final String title,
                   @JsonProperty("user") final String user,
                   @JsonProperty("info") final String info) {
        this.time = time;
        this.origin = origin;
        this.title = title;
        this.user = user;
        this.level = info;
    }
}
