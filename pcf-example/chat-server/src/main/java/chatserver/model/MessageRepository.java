package chatserver.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
//https://github.com/shekhargulati/redis-dictionary/blob/master/src/main/java/com/shekhar/redis/dictionary/DictionaryDao.java
//https://spring.io/guides/gs/accessing-data-jpa/
//https://github.com/spring-projects/spring-data-examples/tree/master/redis/repositories/src/main/java/example/springdata/redis/repositories
public interface MessageRepository extends CrudRepository<Message, String> {

    /**
    private static final String MESSAGES = "messages";
    private RedisTemplate<String, String> template;


    @Autowired
    public MessageRepository(RedisTemplate<String, String> template) {
        this.template = template;
    }

    public Set<String> allMessages() {
        Set<String> messages = template.opsForSet().members(
                MESSAGES);
        return messages;
    }

    public void addMessage(Message message) {

        template.opsForList().rightPush(MESSAGES, message);
        template.opsForSet().add(ALL_UNIQUE_WORDS, word);
        template.opsForSet().add(partOfSpeech, word);
        return index;
    }
    */

}
