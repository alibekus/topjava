package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

public class MealRestControllerTest extends AbstractControllerTest {

    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    public void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void getAll() throws Exception {
        final List<MealTo> tos = getTos(MEALS, MealsUtil.DEFAULT_CALORIES_PER_DAY);
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(tos));
    }

    @Test
    void filter() throws Exception {
        final List<Meal> betweenDates = mealService.getBetweenDates(LocalDate.of(2015, 05, 30),
                LocalDate.of(2015, 05, 30),
                SecurityUtil.authUserId());
        final List<MealTo> tos = getTos(betweenDates, MealsUtil.DEFAULT_CALORIES_PER_DAY);
        mockMvc.perform(MockMvcRequestBuilders
                .get(REST_URL + "filter?startDate=2015-05-30&startTime=00:00&endDate=2015-05-30&endTime=23:59"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(tos));
    }

    @Test
    void filterAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "filter?startDate=&endTime="))
                        .andExpect(status().isOk())
                        .andExpect(contentJson(getTos(MEALS, MealsUtil.DEFAULT_CALORIES_PER_DAY)));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = readFromJson(action, Meal.class);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, SecurityUtil.authUserId()), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        assertMatch(mealService.get(MEAL1_ID, SecurityUtil.authUserId()), updated);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, SecurityUtil.authUserId()));
    }

}
