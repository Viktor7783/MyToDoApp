package com.korotkov.todoapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users", schema = "MyToDoApp")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)//Lombok будет учитывать только те поля, которые ты явно пометишь!
public class User {
    @Id
    @EqualsAndHashCode.Include //будет сравниваться только id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 20)
    @Builder.Default
    private String lastName = "";
    @Column(name = "username", nullable = false, length = 250, unique = true)
    private String username;
    @Column(name = "password", nullable = false, length = 255)
    @ToString.Exclude
    private String password;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ToDo> tasks = new HashSet<>();

    public void addTask(ToDo task) {
        tasks.add(task);
        task.setUser(this);
    }

    public void removeTask(ToDo task) {
        tasks.remove(task);
        task.setUser(null);
    }

}
