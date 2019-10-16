package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MealRestController.class);

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal get(int userId, int id) {
        LOGGER.info("get meal {} ", id);
        return service.get(userId, id);
    }

    public Meal create(int userId, Meal meal) {
        LOGGER.info("create meal {}", meal);
        checkNew(meal);
        return service.create(userId, meal);
    }

    public void delete(int userId, int id) {
        LOGGER.info("delete meal {}", id);
        service.delete(userId, id);
    }

    public void update(int userId, int id, Meal meal) {
        LOGGER.info("update meal {}", id);
        assureIdConsistent(meal, id);
        service.update(userId, meal);
    }

    public List<Meal> getAll(int userId) {
        LOGGER.info("getAll({})", userId);
        return service.getAll(userId);
    }
}