package ru.javawebinar.topjava.service.datajpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.UserServiceTest;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.Profiles.JPA_DATAJPA;

@ActiveProfiles({JPA_DATAJPA, DATAJPA})
public class DataJpaUserServiceTest extends UserServiceTest {
}
