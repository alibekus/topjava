package ru.javawebinar.topjava.rule;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TestStopwatchRule extends Stopwatch {

    private final Map<String, Long> testTimeMap;

    public TestStopwatchRule(Map<String, Long> testTimeMap) {
        this.testTimeMap = testTimeMap;
    }

    /**
     * Invoked when a test method finishes (whether passing or failing)
     */
    protected void finished(long nanos, Description description) {
        testTimeMap.put(description.getMethodName(), TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS));
    }
}
