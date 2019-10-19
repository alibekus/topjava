package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    public static final List<Meal> USER_MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 8, 0), "Завтрак", 850),
            new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 13, 30), "Обед", 1230),
            new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 19, 30), "Ужин", 940),
            new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 7, 40), "Завтрак", 630),
            new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 13, 10), "Обед", 1530),
            new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 19, 25), "Ужин", 375)
    );

    public static final List<Meal> ADMIN_MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 7, 30), "Завтрак", 740),
            new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 12, 30), "Обед", 1160),
            new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 18, 45), "Ужин", 670),
            new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 7, 35), "Завтрак", 475),
            new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 13, 5), "Обед", 1310),
            new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 19, 45), "Ужин", 590)
    );

    public static final Meal TEST_MEAL = new Meal(LocalDateTime.of(2019, 3, 5, 7, 45), "Breakfast", 555);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("id").isEqualTo(expected);
    }
}
