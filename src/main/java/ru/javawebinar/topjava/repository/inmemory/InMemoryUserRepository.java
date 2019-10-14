package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        logger.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        logger.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldMeal) -> user);
    }

    @Override
    public User get(int id) {
        logger.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        logger.info("getAll");
        Collection<User> values = repository.values().stream()
                .sorted(Comparator.comparing(AbstractNamedEntity::getName))
                .collect(Collectors.toList());
        return new ArrayList<>(values);
    }

    @Override
    public User getByEmail(String email) {
        logger.trace("getByEmail {}", email);
        User userEmail = null;
        try {
            userEmail = repository.values().stream().
                    filter(user -> user.getEmail().equals(email)).findFirst().get();
        } catch (NoSuchElementException exp) {
            logger.error("Didn't find a user with the searched email.", exp);
            exp.printStackTrace();
        } finally {
            return userEmail;
        }
    }
}
