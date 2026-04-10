package com.korotkov.todoapp.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "todos", schema = "MyToDoApp")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)//Lombok будет учитывать только те поля, которые ты явно пометишь!
public class ToDo {
    @Id
    @EqualsAndHashCode.Include //будет сравниваться только id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description", nullable = false, length = 250)
    private String description;
    @Column(name = "is_done", nullable = false)
    @Builder.Default
    boolean isDone = false;
    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;
    @Column(name = "title", length = 100)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
