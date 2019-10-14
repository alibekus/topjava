package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.user.AbstractUserController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger logger = getLogger(UserServlet.class);

    private AbstractUserController controller;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            controller = appCtx.getBean(AdminRestController.class);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("forward to users");
        String userId = request.getParameter("user-id");
        logger.info("User id {}", userId);
        SecurityUtil.setAuthUserId(Integer.parseInt(userId));
        request.getRequestDispatcher("meals").forward(request, response);
    }
}
