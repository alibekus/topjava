package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MealServiceTest.class.getSimpleName());
    private static final int USER_MEAL_1_ID = 100_002;

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        LOGGER.info("get meal of user {}", USER_ID);
        Meal actualMeal = service.get(USER_MEAL_1.getId(), USER_ID);
        assertThat(actualMeal).isEqualToIgnoringGivenFields(actualMeal, "id")
                .isEqualTo(USER_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        final Meal testMeal = new Meal(TEST_MEAL.getDateTime(), TEST_MEAL.getDescription(), TEST_MEAL.getCalories());
        Meal createdMeal = service.create(testMeal, USER_ID);
        final Integer id = createdMeal.getId();
        LOGGER.info("delete meal with id {}", id);
        service.delete(id, USER_ID);
        service.get(id, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        LocalDate startDate = LocalDate.of(2019, 10, 4);
        LocalDate endDate = LocalDate.of(2019, 3, 5);
        List<Meal> mealsBetweenDates = service.getBetweenDates(startDate, endDate, USER_ID);
        int size = mealsBetweenDates.size();
        if (size > 0) {
            LocalDate firstMealDate = mealsBetweenDates.get(0).getDate();
            assertThat(firstMealDate).isAfterOrEqualTo(startDate);
            assertThat(firstMealDate).isBeforeOrEqualTo(endDate);
            if (size > 1) {
                LocalDate lastMealDate = mealsBetweenDates.get(size - 1).getDate();
                assertThat(lastMealDate).isAfterOrEqualTo(startDate);
                assertThat(lastMealDate).isBeforeOrEqualTo(endDate);
            }
        }
    }

    @Test
    public void getAll() {
        final List<Meal> userMealsDb = service.getAll(USER_ID);
        final Meal[] userMealsSorted = USER_MEALS.toArray(new Meal[USER_MEALS.size()]);
        for (int i = 0; i < userMealsDb.size(); i++) {
            userMealsSorted[i].setId(userMealsDb.get(i).getId());
        }
        LOGGER.info(userMealsDb.toString());
        assertMatch(userMealsDb, userMealsSorted);
    }

    @Test
    public void update() {
        Meal actualMeal = service.get(USER_MEAL_1_ID, USER_ID);
        LOGGER.info("actual {}", actualMeal.toString());
        Meal updateMeal = new Meal(TEST_MEAL.getDateTime(), TEST_MEAL.getDescription(), TEST_MEAL.getCalories());
        updateMeal.setId(USER_MEAL_1_ID);
        service.update(updateMeal, USER_ID);
        LOGGER.info("update meal for user {}", service.get(USER_MEAL_1_ID, USER_ID).toString());
        assertMatch(service.get(USER_MEAL_1_ID, USER_ID), updateMeal);
    }

    @Test
    public void create() {
        final Meal testMeal = new Meal(TEST_MEAL.getDateTime(), TEST_MEAL.getDescription(), TEST_MEAL.getCalories());
        LOGGER.info("create meal for user {}", USER_ID);
        Meal expectedMeal = service.create(testMeal, USER_ID);
        final Meal actualMeal = service.get(expectedMeal.getId(), USER_ID);
        service.delete(expectedMeal.getId(), USER_ID);
        assertThat(actualMeal)
                .isEqualToComparingOnlyGivenFields(expectedMeal,
                        "dateTime", "description", "calories");
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUserMeal() {
        LOGGER.info("getAnotherUserMeal()");
        service.get(USER_MEAL_1_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAnotherUserMeal() {
        service.delete(USER_MEAL_1_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateAnotherUserMeal() {
        final Meal userMeal = service.get(USER_MEAL_1_ID, USER_ID);
        service.update(userMeal, ADMIN_ID);
    }
}