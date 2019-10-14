package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger logger = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<Meal> getAll() {
        logger.info("getAll meal of all users");
        return service.getAll();
    }

    public Meal get(int id) {
        logger.info("get meal {} ", id);
        Meal meal = null;
        try {
            meal = service.get(id, authUserId());
        } catch (NotFoundException exp) {
            logger.error(exp.getLocalizedMessage(),exp);
            exp.printStackTrace();
        }
        return meal;
    }

    public Meal create(Meal meal) {
        logger.info("create meal {}", meal);
        checkNew(meal);
        return service.create(meal);
    }

    public void delete(int id) {
        logger.info("delete meal {}", id);
        try {
            service.delete(id, authUserId());
        } catch (NotFoundException exp) {
            logger.error(exp.getLocalizedMessage(),exp);
            exp.printStackTrace();
        }
    }

    public void update(Meal meal, int id) {
        logger.info("update meal {}", id);
        assureIdConsistent(meal, id);
        try {
            service.update(meal);
        } catch (NotFoundException exp) {
            logger.error(exp.getLocalizedMessage(), exp);
            exp.printStackTrace();
        }
    }

    public List<Meal> getAll(int userId) {
        logger.info("getAll({})", userId);
        return service.getAll(userId).stream().collect(Collectors.toList());
    }

    public List<MealTo> getTosBetweenDateAndTime(List<MealTo> mealTos, LocalDateTime start, LocalDateTime end) {
        logger.info("getBetweenDateTime between {} and {}", start, end);
        return service.getTosBetweenDateAndTime(mealTos, start, end);
    }

}