package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void delete(int userId, int id) throws NotFoundException {
        Meal meal = get(userId, id);
        if (meal != null) {
            checkNotFoundWithId(repository.delete(userId, id), id);
        }
    }

    public Meal get(int userId, int id) throws NotFoundException {
        Meal meal = checkNotFoundWithId(repository.get(userId, id), id);
        if (userId != meal.getUserId()) {
            throw new NotFoundException("The meal is not belong to current user");
        }
        return meal;
    }

    public List<Meal> getAll(int userId) throws NotFoundException {
        return repository.getAll(userId);
    }

    public void update(int userId, Meal meal) {
        Meal meal1 = get(userId, meal.getId());
        if (meal1 != null) {
            checkNotFoundWithId(repository.save(userId, meal), meal.getId());
        }
    }
}