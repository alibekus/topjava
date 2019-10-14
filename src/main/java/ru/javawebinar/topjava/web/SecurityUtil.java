package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.UsersUtil;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static final Logger log = getLogger(SecurityUtil.class);

    private static int userId;

    private SecurityUtil() { throw new IllegalStateException("Utility class");}

    public static int authUserId() {
        return userId;
    }

    public static void setAuthUserId(int id) {
        log.info("set user id: " + id);
        userId = id;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}