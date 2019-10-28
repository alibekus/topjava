package ru.javawebinar.topjava.rules;

import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MealTestStatement extends Statement {

    private final Statement base;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public MealTestStatement( Statement base ) {
        this.base = base;
    }

    @Override
    public void evaluate() throws Throwable {
        logger.info("Test starts");
        long startMillis = System.currentTimeMillis();
        try {
            base.evaluate();
        } finally {
            long endMillis = System.currentTimeMillis();
            long testMillis = endMillis - startMillis;
            logger.info("Test ends. It executed {} ms", testMillis);
        }
    }
}
