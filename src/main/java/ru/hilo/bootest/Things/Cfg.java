package ru.hilo.bootest.Things;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Cfg {

    public static final Cfg SIEBEL = new Cfg();

    private Cfg() {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);

            this.url  = property.getProperty("db.url");
            this.login  = property.getProperty("db.login");
            this.pass = property.getProperty("db.password");
            this.driver  = property.getProperty("db.driver");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }

    public String url;
    public String login;
    public String pass;
    public String driver;
}
