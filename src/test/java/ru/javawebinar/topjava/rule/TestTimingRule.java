package ru.javawebinar.topjava.rule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class TestTimingRule implements TestRule {
    @Override
    public Statement apply(Statement base, Description description) {
        return new TestTimingStatement(base);
    }
}
