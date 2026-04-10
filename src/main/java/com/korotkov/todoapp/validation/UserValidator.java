package com.korotkov.todoapp.validation;

import com.korotkov.todoapp.dao.UserDao;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class UserValidator {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String errorMessage;
    private final UserDao userDao;

    public UserValidator(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean isValid(HttpServletRequest request) {
        firstName = request.getParameter("firstName");
        lastName = request.getParameter("lastName");
        username = request.getParameter("username");
        password = request.getParameter("password");

        if (username == null || username.isBlank()) {
            errorMessage = "Имя пользователя не передано";
            return false;
        }
        if (password == null || password.isBlank()) {
            errorMessage = "Пароль не передан";
            return false;
        }

        username = username.trim();
        password = password.trim();
        if (firstName != null) firstName = firstName.trim();
        if (lastName != null) lastName = lastName.trim();

        if (!checkUsername()) return false;
        return checkPassword();

    }

    private boolean checkUsername() {
        if (!username.matches("^[a-zA-Z0-9_-]{3,20}$")) {
            errorMessage = "Имя пользователя должно быть 3–20 символов. Разрешены буквы, цифры, _ и -";
            return false;
        }

        if (userDao.getUserByName(username) != null) {
            errorMessage = "Пользователь уже существует";
            return false;
        }
        return true;
    }

    private boolean checkPassword() {
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%?])[A-Za-z\\d!@#$%?]{8,20}$")) {
            errorMessage = """
                    Пароль должен содержать:
                    • 8–20 символов
                    • одну заглавную букву
                    • одну строчную букву
                    • одну цифру
                    • один символ !@#$%?""";
            return false;
        }
        return true;
    }
}

/*
Проверка сложного пароля с помощью регулярного выражения:
^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%?])[A-Za-z\d!@#$%?]{8,20}$
Разбор:
^ - начало строки
(?=.*[a-z]) - Lookahead-проверка: строка должна содержать хотя бы одну маленькую букву (a-z)
(?=.*[A-Z]) - строка должна содержать хотя бы одну большую букву (A-Z)
(?=.*\d) - строка должна содержать хотя бы одну цифру (\d = digit (0-9))
(?=.*[!@#$%?]) - строка должна содержать хотя бы один спецсимвол из списка: ! @ # $ % ?
[A-Za-z\d!@#$%?] - разрешённые символы в пароле: большие буквы A-Z; маленькие буквы a-z; цифры 0-9; спецсимволы ! @ # $ % ?
{8,20} - длина строки: минимум 8 символов; максимум 20 символов
$ - конец строки

Итог:
пароль должен
✔ содержать маленькую букву
✔ содержать большую букву
✔ содержать цифру
✔ содержать спецсимвол
✔ иметь длину 8–20 символов
✔ состоять только из разрешённых символов
*/