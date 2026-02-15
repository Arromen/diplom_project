package org.example.diplom_project_2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // например: "IT-СИП-22-23-Java"
}
