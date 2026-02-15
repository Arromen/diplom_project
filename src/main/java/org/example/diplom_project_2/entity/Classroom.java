package org.example.diplom_project_2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classrooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // например, "304", "Лекционная 1"

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String type; // "lecture", "lab", "computer", "seminar"

    // Можно позже добавить связи с features (проектор и т.д.)
}
