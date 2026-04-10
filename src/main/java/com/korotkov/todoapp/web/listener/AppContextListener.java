package com.korotkov.todoapp.web.listener;

import com.korotkov.todoapp.utils.HibernateUtils;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateUtils.closeSessionFactory();
        AbandonedConnectionCleanupThread.checkedShutdown();
    }
}

/* Класс помечен аннотацией:
@WebListener
Это значит:
• Tomcat сам найдёт этот класс при запуске приложения
• сам создаст его объект
• сам вызовет нужные методы жизненного цикла

То есть вручную делать что-то вроде: new AppContextListener() не нужно!*/
