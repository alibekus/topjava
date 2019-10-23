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
        Meal actualMeal = service.get(USER_MEAL_1_ID, USER_ID);
        assertMatch(actualMeal, USER_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        Meal createdMeal = service.create(getCreateMeal(), USER_ID);
        final Integer id = createdMeal.getId();
        service.delete(id, USER_ID);
        service.get(id, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        LocalDate startDate = LocalDate.of(2019, 3, 5);
        LocalDate endDate = LocalDate.of(2019, 10, 4);
        List<Meal> mealsBetweenDates = service.getBetweenDates(startDate, endDate, USER_ID);
        assertMatch(mealsBetweenDates, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getAll() {
        final List<Meal> actualMeals = service.getAll(USER_ID);
        LOGGER.info(actualMeals.toString());
        assertMatch(actualMeals, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void update() {
        Meal updateMeal = getUpdateMeal();
        final Meal mealBeforeUpdate = service.get(updateMeal.getId(), USER_ID);
        service.update(updateMeal, USER_ID);
        final Meal mealAfterUpdate = service.get(USER_MEAL_1_ID, USER_ID);
        service.update(mealBeforeUpdate, USER_ID);
        assertMatch(mealAfterUpdate, updateMeal);
    }

    @Test
    public void create() {
        Meal expectedMeal = service.create(getCreateMeal(), USER_ID);
        final Meal actualMeal = service.get(expectedMeal.getId(), USER_ID);
        service.delete(expectedMeal.getId(), USER_ID);
        assertMatch(actualMeal, expectedMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUserMeal() {
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