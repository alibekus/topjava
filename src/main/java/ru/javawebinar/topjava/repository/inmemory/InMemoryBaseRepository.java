package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.AbstractBaseEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryBaseRepository<T extends AbstractBaseEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryBaseRepository.class);
    private Map<Integer, T> repository = new ConcurrentHashMap<>();
    protected final AtomicInteger COUNTER = new AtomicInteger(0);

    public InMemoryBaseRepository() {
    }

    public InMemoryBaseRepository(Map<Integer, T> repository) {
        this.repository = repository;
    }

    public T save(Integer entityId, T entity) {
        if (entity.isNew()) {
            entity.setId(COUNTER.incrementAndGet());
            repository.put(entity.getId(), entity);
            return entity;
        }
        // treat case: update, but not present in storage
        return repository.computeIfPresent(entityId, (id, oldMeal) -> entity);
    }

    public boolean delete(int id) {
        return repository.remove(id) != null;
    }

    public T get(int id) {
        return repository.get(id);
    }

    public List<T> getValues() {
        return repository.values().stream().collect(Collectors.toList());
    }
}

