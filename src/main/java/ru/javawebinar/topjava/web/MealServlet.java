package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class MealServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            controller = appCtx.getBean(MealRestController.class);
        }
        List<Meal> allMeal = MealsUtil.MEALS;
        for (int i = 0; i < allMeal.size(); i++) {
            controller.create(allMeal.get(i));
            logger.info(allMeal.get(i).toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        meal.setUserId(authUserId());
        logger.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            controller.create(meal);
        } else {
            controller.update(meal, meal.getId());
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                logger.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                logger.info("meal id {}", meal.getId());
                logger.info("meal user id {}", meal.getUserId());
                break;
            case "filter":
                logger.info("getFiltered by date and time");
                String dateFromString = request.getParameter("date-from");
                String dateToString = request.getParameter("date-to");
                String timeFromString = request.getParameter("time-from");
                String timeToString = request.getParameter("time-to");
                logger.info("From date: {} and time: {}", dateFromString, timeFromString);
                logger.info("To date: {} and time: {}", dateToString, timeToString);
                final List<LocalDate> dates = DateTimeUtil.convertToDates(dateFromString, dateToString);
                List<LocalTime> times = DateTimeUtil.convertToTime(timeFromString, timeToString);
                final LocalDateTime dateTimeFrom = LocalDateTime.of(dates.get(0), times.get(0));
                final LocalDateTime dateTimeTo = LocalDateTime.of(dates.get(1), times.get(1));
                final List<MealTo> mealTos = MealsUtil.getTos(controller.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
                final List<MealTo> tosBetweenDateAndTime = controller.getTosBetweenDateAndTime(mealTos, dateTimeFrom, dateTimeTo);
                request.setAttribute("meals", tosBetweenDateAndTime);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                logger.info("getAll of userId {} ", authUserId());
                final List<MealTo> tos = MealsUtil.getTos(controller.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
                tos.stream().forEach(to -> logger.info(to.toString()));
                request.setAttribute("userId", authUserId());
                request.setAttribute("meals",
                        tos);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
