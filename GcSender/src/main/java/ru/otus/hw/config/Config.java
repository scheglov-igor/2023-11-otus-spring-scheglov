package ru.otus.hw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties // IDEA ругается, но работает
public class Config {

    //TODO сделать геттеры для нескольких интерфейсов - для гц, для базы, для других настроек ???

    // ####################################
    // ### local settings (no default!) ###
    // ####################################
    private Boolean postgresVersion = false;

    private String dbUrl = null;

    private String dbServiceName = null;

    private String dbUser = null;

    private String gcUrl = null;

    private String gcUrlAlt = null;

    private String gcLogin = null;

    private String gcPassword = null;


    // #####################################
    // ### common settings with defaults ###
    // #####################################
    private Boolean testGcData = false;

    private String dbPort = "5431";

    private String dbPassword = ""; //TODO

    private Boolean debug = false;

    private Boolean noCheckCertificate = false;

    private Integer senderScheduleSec = 600;


    public String getJdbcUrl() {

        String dbConnection = null;

        if (postgresVersion) {
            dbConnection = "jdbc:postgresql://" + dbUrl + ":" + dbPort + "/" +
                    dbServiceName + "?characterEncoding=CP1251&amp;escapeSyntaxCallMode=callIfNoReturn";
        } else {
            dbConnection = "jdbc:oracle:thin:@//" + dbUrl + ":" + dbPort + "/" + dbServiceName;
        }

        return dbConnection;
    }


}
