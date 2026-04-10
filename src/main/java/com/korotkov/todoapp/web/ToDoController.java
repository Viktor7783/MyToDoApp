package com.korotkov.todoapp.web;

import com.korotkov.todoapp.dao.ToDoDao;
import com.korotkov.todoapp.dao.ToDoDaoImpl;
import com.korotkov.todoapp.model.ToDo;
import com.korotkov.todoapp.model.User;
import com.korotkov.todoapp.validation.ToDoValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/")
public class ToDoController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ToDoDao toDoDao;

    public void init() {
        toDoDao = new ToDoDaoImpl();
    }

    public void setToDoDao(ToDoDao toDoDao) {
        this.toDoDao = toDoDao;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            switch (action) {
                case ("/insert") -> insertToDo(request, response);
                case ("/update") -> updateToDo(request, response);
                case ("/delete") -> deleteToDo(request, response);
                default -> response.sendRedirect(request.getContextPath() + "/list");
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            switch (action) {
                case ("/new") -> showNewForm(request, response);
                case ("/edit") -> showEditForm(request, response);
                case ("/list") -> listToDo(request, response);
                default -> response.sendRedirect(request.getContextPath() + "/list");
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listToDo(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        User user = (User) request.getSession().getAttribute("user");
        // Забираем flash-сообщение из session
        String error = (String) request.getSession().getAttribute("ERROR");
        if (error != null) {
            request.setAttribute("ERROR", error);
            request.getSession().removeAttribute("ERROR");
        }

        List<ToDo> listToDo = toDoDao.selectAllToDosByUser(user);
        request.setAttribute("listToDo", listToDo);
        request.getRequestDispatcher("/todo-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/todo-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        ToDo existingToDo = getOwnedToDoOrRedirect(request, response, id);
        if (existingToDo == null) return;

        request.setAttribute("todo", existingToDo);
        request.getRequestDispatcher("/todo-form.jsp").forward(request, response);
    }

    private void insertToDo(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        User user = (User) request.getSession().getAttribute("user");
        ToDoValidator validator = new ToDoValidator(); // Проверяем title и description
        if (!validator.isValid(request)) {
            request.setAttribute("ERROR", validator.getErrorMessage());
            request.setAttribute("title", validator.getTitle());
            request.setAttribute("description", validator.getDescription());
            request.getRequestDispatcher("/todo-form.jsp").forward(request, response);
            return;
        }
        LocalDate targetDate = LocalDate.parse(request.getParameter("targetDate"));
        boolean isDone = Boolean.parseBoolean(request.getParameter("isDone"));
        ToDo newToDo = ToDo.builder().title(validator.getTitle()).user(user).description(validator.getDescription()).isDone(isDone).targetDate(targetDate).build();
        try {
            toDoDao.insertToDo(newToDo);
            response.sendRedirect(request.getContextPath() + "/list");
        } catch (Exception e) {
            request.setAttribute("todo", newToDo);
            request.setAttribute("ERROR", "Ошибка при создании задачи");
            request.getRequestDispatcher("/todo-form.jsp").forward(request, response);
        }
    }

    private void updateToDo(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        long id = Long.parseLong(request.getParameter("id"));
        ToDo existingToDo = getOwnedToDoOrRedirect(request, response, id);
        if (existingToDo == null) return;
        ToDoValidator validator = new ToDoValidator(); // Проверяем title и description
        if (!validator.isValid(request)) {
            request.setAttribute("ERROR", validator.getErrorMessage());
            request.setAttribute("title", validator.getTitle());
            request.setAttribute("description", validator.getDescription());
            request.getRequestDispatcher("/todo-form.jsp").forward(request, response);
            return;
        }
        existingToDo.setTitle(validator.getTitle());
        existingToDo.setDescription(validator.getDescription());
        existingToDo.setTargetDate(LocalDate.parse(request.getParameter("targetDate")));
        existingToDo.setDone(Boolean.parseBoolean(request.getParameter("isDone")));
        try {
            toDoDao.updateToDo(existingToDo);
            response.sendRedirect(request.getContextPath() + "/list");//Чтобы не было повторной отправки формы
        } catch (Exception e) {
            request.setAttribute("todo", existingToDo);
            request.setAttribute("ERROR", "Ошибка при обновлении задачи");
            request.getRequestDispatcher("/todo-form.jsp").forward(request, response);
        }
    }


    private void deleteToDo(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        long id = Long.parseLong(request.getParameter("id"));
        ToDo existingToDo = getOwnedToDoOrRedirect(request, response, id);
        if (existingToDo == null) return;
        try {
            toDoDao.deleteToDo(id);
            response.sendRedirect(request.getContextPath() + "/list");
        } catch (Exception e) {
            request.getSession().setAttribute("ERROR", "Ошибка при удалении задачи");
            response.sendRedirect(request.getContextPath() + "/list");
        }
    }

    private ToDo getOwnedToDoOrRedirect(HttpServletRequest request, HttpServletResponse response, long id) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        ToDo existingToDo = toDoDao.selectToDoByIdAndUserId(id, user.getId());

        if (existingToDo == null) {
            response.sendRedirect(request.getContextPath() + "/list");
            return null;
        }
        return existingToDo;
    }
}
