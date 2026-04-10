package com.korotkov.todoapp.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            /*Hibernate создаёт объект конфигурации и читает настройки из файла hibernate.cfg.xml = то есть сначала берёт “базовые” параметры из XML*/
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            /*потом программа смотрит переменные окружения: DB_URL, DB_USERNAME, DB_PASSWORD
              если они существуют и не пустые, то они заменяют значения из hibernate.cfg.xml
              То есть логика такая: по умолчанию настройки берутся из XML
              если снаружи передали переменные окружения, они имеют приоритет*/
            overrideIfPresent(configuration, "hibernate.connection.url", "DB_URL");
            overrideIfPresent(configuration, "hibernate.connection.username", "DB_USERNAME");
            overrideIfPresent(configuration, "hibernate.connection.password", "DB_PASSWORD");
            /*Это удобно для Docker: локально можно работать через hibernate.cfg.xml
              в контейнере можно подставить другие параметры без переписывания файла*/
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static void overrideIfPresent(Configuration configuration, String propertyName, String envName) {
        String value = System.getenv(envName);
        if (value != null && !value.isBlank()) {
            configuration.setProperty(propertyName, value);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (!sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
