package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Meal get(int id, int userId);

    List<Meal> getAll(int userId);

    List<Meal> getAllFilteredSorted();

    List<Meal> getAllFilteredSorted(int userId);

    List<Meal> getBetweenDateTime(int userId, LocalDateTime start, LocalDateTime end);

    List<MealTo> getTosBetweenDateAndTime(List<MealTo> mealTos, LocalDateTime start, LocalDateTime end);

    List<Meal> getBetweenTime(int userId, LocalTime start, LocalTime end);
}
