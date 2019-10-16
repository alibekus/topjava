package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository extends InMemoryBaseRepository<Meal> implements MealRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, InMemoryBaseRepository<Meal>> userMeals = new ConcurrentHashMap<>();

    @Override
    public Meal save(Integer userId, Meal meal) {
        InMemoryBaseRepository<Meal> mealRepository;
        mealRepository = userMeals.computeIfAbsent(userId,
                uid -> new InMemoryBaseRepository<>());
        return mealRepository.save(meal.getId(), meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        InMemoryBaseRepository<Meal> mealRepository = userMeals.get(userId);
        return mealRepository != null && mealRepository.delete(id);
    }

    @Override
    public Meal get(int userId, int id) {
        InMemoryBaseRepository<Meal> mealRepository = userMeals.get(userId);
        return mealRepository == null ? null : mealRepository.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        InMemoryBaseRepository<Meal> meals = userMeals.get(userId);
        return meals == null ? Collections.emptyList() :
                meals.getValues().stream()
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }
}
