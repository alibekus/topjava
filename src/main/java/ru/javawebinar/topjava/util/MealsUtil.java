package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    private MealsUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(null, 1, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак1", 500),
            new Meal(null, 1, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед1", 1000),
            new Meal(null, 1, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин1", 500),
            new Meal(null, 2, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак2", 1000),
            new Meal(null, 2, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед2", 500),
            new Meal(null, 2, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин2", 510),
            new Meal(null, 1, LocalDateTime.of(2017, Month.SEPTEMBER, 23, 8, 10), "Завтрак1", 732),
            new Meal(null, 1, LocalDateTime.of(2017, Month.SEPTEMBER, 23, 12, 33), "Обед1", 1832),
            new Meal(null, 1, LocalDateTime.of(2017, Month.SEPTEMBER, 23, 19, 45), "Ужин1", 896),
            new Meal(null, 2, LocalDateTime.of(2018, Month.DECEMBER, 12, 7, 27), "Завтрак2", 964),
            new Meal(null, 2, LocalDateTime.of(2018, Month.DECEMBER, 12, 13, 4), "Обед2", 1632),
            new Meal(null, 2, LocalDateTime.of(2018, Month.DECEMBER, 18, 18, 55), "Ужин2", 436)
    );

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return getFiltered(meals, caloriesPerDay, meal -> true);
    }

    private static List<MealTo> getFiltered(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );
        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}