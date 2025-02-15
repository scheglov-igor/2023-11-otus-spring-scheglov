package ru.otus.hw.services;

import java.net.URISyntaxException;
import java.sql.Statement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import com.impossibl.postgres.jdbc.PGDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.CriticalStopException;


/**
 * DCN listener: it prints out the event details in stdout.
 */
@Slf4j
@Service
@ConditionalOnProperty(
        name = "postgresVersion",
        havingValue = "true")
@RequiredArgsConstructor
public class PostgresListenerServiceImpl implements ListenerService {
	
	private final TaskPoolService taskPoolService;
	private final EnviromentService enviromentService;

	private PGConnection connection;

    private PGDataSource buildPgDataSource() throws URISyntaxException, CriticalStopException {

        PGDataSource pgDataSource = new PGDataSource();
        pgDataSource.setHost(enviromentService.getConfig().getDbUrl());
        pgDataSource.setPort(Integer.valueOf(enviromentService.getConfig().getDbPort()));
        pgDataSource.setDatabaseName(enviromentService.getConfig().getDbServiceName());
        pgDataSource.setUser(enviromentService.getConfig().getDbUser());
        pgDataSource.setPassword(enviromentService.getConfig().getDbPassword());

        return pgDataSource;
    }
    
    
	
	// следим за изменениями
	@Override
	public void startListener() throws CriticalStopException {

		unregisterAllDatabaseChangeNotification();
		registerDatabaseChangeNotification();
		
	}
	
    


    public void registerDatabaseChangeNotification() throws CriticalStopException {
        log.info("starting PG listener...");
        PGNotificationListener pgNotificationListener = new PGNotificationListener() {

            /**
             * ловим уведомление об изменении
             */
            @Override
            public void notification(int processId, String channelName, String payload) {
                log.info("notification: processId={}, channelName={}, payload={}", processId, channelName, payload);

                Long id = Long.valueOf(payload);

                taskPoolService.addTask(id, null);
            }

            @Override
            public void closed() {
                //TODO
                // initiate reconnection & restart listening
            }

        };


//TODO так в руководстве предлагается создать подключение

//        PGDataSource pgDataSource = (PGDataSource) dataSource;

        //OK
//        PGDataSource pgDataSource = new PGDataSource();
//        pgDataSource.setHost("localhost");
//        pgDataSource.setPort(5430);
//        pgDataSource.setDatabaseName("demoDB");
//        pgDataSource.setUser("usr");
//        pgDataSource.setPassword("pwd");


        try {
            connection = (PGConnection) buildPgDataSource().getConnection();
            connection.addNotificationListener(pgNotificationListener);

            final Statement statement = connection.createStatement();

            statement.execute("LISTEN notify_channel_tlg_send_gc_tb");
            statement.close();
            //TODO ??
//            connection.close();

        } catch (Exception e) {
            throw new CriticalStopException(e);
        }

        //TODO connection.close ();

        log.info("PG listener started");

    }

    //TODO refresh connection? Запрашивать и если не отвечает - переконнктить?

    @Override
	public void unregisterAllDatabaseChangeNotification() throws CriticalStopException {
       
		try {
			if(connection != null) {
                log.info("Unregister previous PG listener...");
				connection.close();
			}
		} catch (Exception e) {
			throw new CriticalStopException(e);
		}
	}
}
