package org.example.diplom_project_2.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class WeekUtils {

    // Возвращает список дат с понедельника по воскресенье для заданной даты
    public static List<LocalDate> getWeekDates(LocalDate date) {
        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        List<LocalDate> week = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            week.add(monday.plusDays(i));
        }
        return week;
    }

    // Возвращает понедельник текущей недели
    public static LocalDate getCurrentMonday() {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
    }
}
