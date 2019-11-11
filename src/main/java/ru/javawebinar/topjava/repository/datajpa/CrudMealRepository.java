package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    Meal save(Meal meal);

    Meal getByIdAndUserId(int id, int userId);

    List<Meal> getAllByUserIdOrderByDateTimeDesc(int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId AND m.dateTime >= :start " +
            "AND m.dateTime < :end ORDER BY m.dateTime DESC")
    List<Meal> getAllBetweenDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                  @Param("userId") int userId);

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id = ?1 and m.user.id = ?2")
    Meal getWithUser(int id, int userId);
}
