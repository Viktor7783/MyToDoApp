package com.korotkov.todoapp.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class ToDoValidator {
    private String title;
    private String description;
    private String errorMessage;

    private final int Max_Title_Length = 100;
    private final int Max_Description_Length = 250;

    public boolean isValid(HttpServletRequest request) {
        title = request.getParameter("title");
        description = request.getParameter("description");
        if (!validateTitle()) return false;
        return validateDescription();
    }

    private boolean validateTitle() {
        if (title == null) {
            errorMessage = "title не может быть пустым";
            return false;
        }
        title = title.trim();

        if (title.isEmpty()) {
            errorMessage = "title не может быть пустым";
            return false;
        }
        if (title.length() > Max_Title_Length) {
            errorMessage = "title не может быть длиннее " + Max_Title_Length + " символов";
            return false;
        }
        return true;
    }

    private boolean validateDescription() {
        if (description == null) {
            errorMessage = "description не может быть пустым";
            return false;
        }
        description = description.trim();

        if (description.isEmpty()) {
            errorMessage = "description не может быть пустым";
            return false;
        }
        if (description.length() > Max_Description_Length) {
            errorMessage = "description не может быть длиннее " + Max_Description_Length + " символов";
            return false;
        }
        return true;
    }
}
