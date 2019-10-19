package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcMealRepository.class.getSimpleName());

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("user_id", userId)
                .addValue("dateTime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories());
        if (userId != SecurityUtil.authUserId()) {
            return null;
        } else if (meal.isNew()) {
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE meals SET date_time=:dateTime, description=:description, " +
                        "calories=:calories WHERE id=:id", map) == 0) {
            return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? and user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT m.id as id, m.date_time as dateTime, " +
                        "m.description as description, m.calories as calories FROM meals m WHERE id=? and m.user_id=?",
                ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        logger.info("getAll({})", userId);
        List<Meal> userMeals = jdbcTemplate.query("SELECT m.id as id, m.date_time as date_time, " +
                "m.description as description, m.calories as calories FROM meals m WHERE m.user_id=? " +
                "ORDER BY m.date_time", ROW_MAPPER, userId);
        logger.info("User's meals: {}", userMeals);
        return userMeals;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return jdbcTemplate.query("SELECT m.id as id, m.date_time as dateTime, " +
                        "m.description as description, m.calories as calories FROM meals m WHERE date_time " +
                        "BETWEEN ? AND ? and m.user_id=?", ROW_MAPPER,
                startDate, endDate, userId);
    }
}
