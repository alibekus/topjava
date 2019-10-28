package ru.javawebinar.topjava.rule;

import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTimingStatement extends Statement {

    private final Statement base;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public TestTimingStatement(Statement base ) {
        this.base = base;
    }

    @Override
    public void evaluate() throws Throwable {
        logger.info("Test method starts");
        long startMillis = System.currentTimeMillis();
        try {
            base.evaluate();
        } finally {
            long endMillis = System.currentTimeMillis();
            long testMillis = endMillis - startMillis;
            logger.info("Test method ends. It executed {} ms", testMillis);
        }
    }
}
