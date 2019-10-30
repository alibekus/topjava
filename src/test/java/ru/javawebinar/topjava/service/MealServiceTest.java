package ru.javawebinar.topjava.service;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.rule.TestStopwatchRule;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private static final Map<String, Long> testTimeMap = new HashMap<>();

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Rule
    public TestStopwatchRule stopwatch = new TestStopwatchRule(testTimeMap);

    @ClassRule
    public static final ExternalResource resource = new ExternalResource() {
        Logger logger = LoggerFactory.getLogger(MealServiceTest.class.getSimpleName());

        @Override
        protected void before() throws Throwable {
            logger.info("Start of MealServiceTest");
        }

        @Override
        protected void after() {
            int totalTime = 0;
            final Set<Map.Entry<String, Long>> testTimeEntry = testTimeMap.entrySet();
            for (Map.Entry testTime : testTimeEntry) {
                final Long time = (Long) testTime.getValue();
                totalTime += time;
                logger.info("{} - {} ms", testTime.getKey(), time);
            }
            logger.info("Total time: {} ms", totalTime);
        }

        ;
    };

    @Autowired
    private MealService service;

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test//(expected = NotFoundException.class)
    public void deleteNotFound() {
        exception.expect(NotFoundException.class);
        service.delete(1, USER_ID);
    }

    @Test//(expected = NotFoundException.class)
    public void deleteNotOwn() {
        exception.expect(NotFoundException.class);
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        final List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test//(expected = NotFoundException.class)
    public void getNotFound() {
        exception.expect(NotFoundException.class);
        service.get(1, USER_ID);
    }

    @Test//(expected = NotFoundException.class)
    public void getNotOwn() {
        exception.expect(NotFoundException.class);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        final Meal actual = service.get(MEAL1_ID, USER_ID);
        assertMatch(actual, updated);
    }

    @Test//(expected = NotFoundException.class)
    public void updateNotFound() {
        exception.expect(NotFoundException.class);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}