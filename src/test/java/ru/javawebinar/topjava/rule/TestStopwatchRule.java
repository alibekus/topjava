package ru.javawebinar.topjava.rule;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestStopwatchRule extends Stopwatch {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final Map<String,Long> testTimeMap;

    public TestStopwatchRule(Map<String,Long> testTimeMap) {
        this.testTimeMap = testTimeMap;
    }

    protected void succeeded(long nanos, Description description) {
        final String methodName = description.getMethodName();
        final Long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
        testTimeMap.put(methodName, millis);
        logger.info("{} succeeded, time taken {} ms", methodName, millis);
    }

    /**
     * Invoked when a test fails
     */
    protected void failed(long nanos, Throwable e, Description description) {
        final String methodName = description.getMethodName();
        final Long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
        testTimeMap.put(methodName, millis);
        logger.info("{} failed, time taken {} ms", description.getMethodName(), millis);
    }

    /**
     * Invoked when a test is skipped due to a failed assumption.
     */
    protected void skipped(long nanos, AssumptionViolatedException e,
                           Description description) {
        final String methodName = description.getMethodName();
        final Long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
        testTimeMap.put(methodName, millis);
        logger.info("{} skipped, time taken {} ms", description.getMethodName(), millis);
}

    /**
     * Invoked when a test method finishes (whether passing or failing)
     */
    protected void finished(long nanos, Description description) {
        final String methodName = description.getMethodName();
        final Long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
        testTimeMap.put(methodName, millis);
    }
}
