package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m " +
                "WHERE m.id = :id AND m.user.id = :userId"),
        @NamedQuery(name = Meal.GET_ALL, query = "SELECT m " +
                "FROM Meal m JOIN FETCH m.user WHERE m.user.id = :userId " +
                "ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.GET_BETWEEN_DATES, query = "SELECT m " +
                "FROM Meal m JOIN FETCH m.user WHERE m.user.id = :userId " +
                "AND m.dateTime BETWEEN :startDate AND :endDate" +
                " ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.FIND, query = "SELECT m " +
                "FROM Meal m JOIN FETCH m.user WHERE m.id = :id AND m.user.id = :userId")
})

@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "meals_user_id_date_time")})
public class Meal extends AbstractBaseEntity {
    public static final String DELETE = "Meal.delete";
    public static final String GET_ALL = "Meal.getAll";
    public static final String GET_BETWEEN_DATES = "Meal.getAllBetweenDates";
    public static final String FIND = "Meal.find";

    @Column(name = "date_time", nullable = false, columnDefinition = "timestamp")
    @NotNull
//    @Convert(converter = LocalTimePersistenceConverter.class)
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false, columnDefinition = "varchar(255)")
    @NotBlank
    @Size(max = 1000)
    private String description;

    @Column(name = "calories", nullable = false, columnDefinition = "int")
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    @NotNull
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
