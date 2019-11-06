package ru.javawebinar.topjava.service.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.Profiles.JPA_DATAJPA;

@ActiveProfiles({JPA_DATAJPA,DATAJPA})
public class DataJpaMealServiceTest extends MealServiceTest {
}
