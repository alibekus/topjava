package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserMealRepository implements MealRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserMealRepository.class);
    Map<Integer, InMemoryMealRepository> userMeals = new ConcurrentHashMap<>();

    @Override
    public Meal save(Meal meal) {
        final Integer userId = meal.getUserId();
        InMemoryMealRepository meals = userMeals.computeIfAbsent(userId, uid -> new InMemoryMealRepository());
        return meals.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        InMemoryMealRepository meals = userMeals.get(userId);
        return meals != null && meals.delete(id);
    }

    @Override
    public Meal get(int id, int userId) {
        InMemoryMealRepository meals = userMeals.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAllFilteredSorted() {
        final Iterator<Map.Entry<Integer, InMemoryMealRepository>> iterator = userMeals.entrySet().iterator();
        List<Meal> meals = new ArrayList<>();
        while (iterator.hasNext()) {
            meals.addAll(iterator.next().getValue().getAll());
        }
        return meals;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return userMeals.get(userId).getValues();
    }

    @Override
    public List<Meal> getAllFilteredSorted(int userId) {
        return getAllFilteredSorted(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetweenDateTime(int userId, LocalDateTime start, LocalDateTime end) {
        return userMeals.get(userId).getValues().stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), start, end))
                .collect(Collectors.toList());
    }

    @Override
    public List<MealTo> getTosBetweenDateAndTime(List<MealTo> mealTos, LocalDateTime start, LocalDateTime end) {
        return mealTos.stream()
                .filter(mealTo -> DateTimeUtil.isBetween(mealTo.getDate(), start.toLocalDate(), end.toLocalDate()))
                .filter(mealTo -> DateTimeUtil.isBetween(mealTo.getTime(), start.toLocalTime(), end.toLocalTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetweenTime(int userId, LocalTime start, LocalTime end) {
        return userMeals.get(userId).getValues().stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), start, end))
                .collect(Collectors.toList());
    }

    private List<Meal> getAllFilteredSorted(int userId, Predicate<Meal> filter) {
        logger.info("getAllFilteredSorted({})", userId);
        InMemoryMealRepository meals = userMeals.get(userId);
        logger.info("meals: {}", meals.toString());
        return meals == null ? Collections.emptyList() :
                meals.getValues().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }
}
