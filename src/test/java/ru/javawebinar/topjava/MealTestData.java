package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    public static final Meal USER_MEAL_1 = new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 8, 0), "Завтрак", 850);
    public static final Meal USER_MEAL_2 = new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 13, 30), "Обед", 1230);
    public static final Meal USER_MEAL_3 = new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 19, 30), "Ужин", 940);
    public static final Meal USER_MEAL_4 = new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 7, 40), "Завтрак", 630);
    public static final Meal USER_MEAL_5 = new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 13, 10), "Обед", 1530);
    public static final Meal USER_MEAL_6 = new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 19, 25), "Ужин", 375);

    public static final List<Meal> USER_MEALS;

    static {
        USER_MEALS = Arrays.asList(
                USER_MEAL_1, USER_MEAL_2, USER_MEAL_3,
                USER_MEAL_4, USER_MEAL_5, USER_MEAL_6
        );
    }

    public static final Meal ADMIN_MEAL_1 = new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 7, 30), "Завтрак", 740);
    public static final Meal ADMIN_MEAL_2 = new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 12, 30), "Обед", 1160);
    public static final Meal ADMIN_MEAL_3 = new Meal(LocalDateTime.of(2019, Month.MARCH, 5, 18, 45), "Ужин", 670);
    public static final Meal ADMIN_MEAL_4 = new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 7, 35), "Завтрак", 475);
    public static final Meal ADMIN_MEAL_5 = new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 13, 5), "Обед", 1310);
    public static final Meal ADMIN_MEAL_6 = new Meal(LocalDateTime.of(2019, Month.APRIL, 10, 19, 45), "Ужин", 590);

    public static final List<Meal> ADMIN_MEALS;

    static {
        ADMIN_MEALS = Arrays.asList(
                ADMIN_MEAL_1, ADMIN_MEAL_2, ADMIN_MEAL_3,
                ADMIN_MEAL_4, ADMIN_MEAL_5, ADMIN_MEAL_6
        );
    }

    public static final Meal TEST_MEAL = new Meal(LocalDateTime.of(2019, 3, 5, 7, 45), "Breakfast", 555);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
