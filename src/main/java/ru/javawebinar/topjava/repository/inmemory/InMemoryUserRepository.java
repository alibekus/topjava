package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository extends InMemoryBaseRepository<User> implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();

    @Override
    public boolean delete(int id) {
        logger.info("delete {}", id);
        return super.delete(id);
    }

    @Override
    public User save(User user) {
        logger.info("save {}", user);
        if (user.isNew()) {
            user.setId(COUNTER.incrementAndGet());
        }
        return super.save(user.getId(), user);
    }

    @Override
    public User get(int id) {
        logger.info("get {}", id);
        return super.get(id);
    }

    @Override
    public List<User> getAll() {
        logger.info("getAll");
        return super.getValues().stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        logger.trace("getByEmail {}", email);
        return repository.values().stream().
                filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }
}
