package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.AbstractBaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryBaseRepository<T extends AbstractBaseEntity> {

    private Map<Integer, T> repository = new ConcurrentHashMap<>();
    protected static final AtomicInteger counter = new AtomicInteger(0);

    public InMemoryBaseRepository() {
    }

    public T save(Integer entityId, T entity) {
        if (entity.isNew()) {
            entity.setId(counter.incrementAndGet());
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
        return new ArrayList<>(repository.values());
    }
}

