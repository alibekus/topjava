package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    public boolean delete(int id) {
        return repository.remove(id) != null;
    }

    public Meal get(int id) {
        return repository.get(id);
    }

    public Collection<Meal> getAll() {
        return repository.values().stream().collect(Collectors.toList());
    }

    public List<Meal> getBetweenDateTime(LocalDateTime start, LocalDateTime end) {
        return repository.values().stream().filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), start, end)).collect(Collectors.toList());
    }

    public List<Meal> getBetweenTime(LocalTime start, LocalTime end) {
        return repository.values().stream().filter(meal -> DateTimeUtil.isBetween(meal.getTime(), start, end)).collect(Collectors.toList());
    }

    List<Meal> getValues() {
        return repository.values().stream().collect(Collectors.toList());
    }
}

