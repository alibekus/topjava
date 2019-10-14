package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public void delete(int id, int userId) throws NotFoundException {
        Meal meal = get(id, userId);
        if (meal != null) {
            checkNotFoundWithId(repository.delete(id, userId), id);
        }
    }

    public Meal get(int id, int userId) throws NotFoundException {
        Meal meal = checkNotFoundWithId(repository.get(id, userId), id);
        if (userId != meal.getUserId()) {
            throw new NotFoundException("The meal is not belong to current user");
        }
        return meal;
    }

    public Collection<Meal> getAll(int id) throws NotFoundException {
        return repository.getAllFilteredSorted(id);
    }

    public void update(Meal meal) {
        Meal meal1 = get(meal.getId(), meal.getUserId());
        if (meal1 != null) {
            checkNotFoundWithId(repository.save(meal), meal.getId());
        }
    }

    public List<Meal> getAll() {
        return repository.getAllFilteredSorted().stream().collect(Collectors.toList());
    }

    public List<MealTo> getTosBetweenDateAndTime(List<MealTo> mealTos, LocalDateTime start, LocalDateTime end) {
        return repository.getTosBetweenDateAndTime(mealTos, start, end);
    }

    public List<Meal> getBetweenTime(int userId, LocalTime start, LocalTime end) {
        return repository.getBetweenTime(userId, start, end);
    }

}