package chatserver.model;

import org.springframework.data.redis.core.RedisHash;

import javax.annotation.sql.DataSourceDefinition;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@RedisHash("messages")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String author;
    private String content;

    public Message() {}

    public Message(Long id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public Message(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}