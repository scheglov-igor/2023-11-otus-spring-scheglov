package ru.otus.hw.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeEvent;
import oracle.jdbc.dcn.DatabaseChangeListener;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.dcn.QueryChangeDescription;
import oracle.jdbc.dcn.RowChangeDescription;
import oracle.jdbc.dcn.TableChangeDescription;
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
        havingValue = "false")
@RequiredArgsConstructor
public class OracleListenerServiceImpl implements DatabaseChangeListener, ListenerService {

    private final TaskPoolService taskPoolService;
    private final EnviromentService enviromentService;


    public OracleConnection getOracleConnection() throws CriticalStopException {

        OracleConnection oracleConnection = null;
        int tryCount = 0;
        int waitTime = 5;
        boolean needDownload = true;
        Exception lastException = null;
        while (needDownload && (tryCount <= 3)) {

            try {
                log.info("connecting {} as {}...", enviromentService.getConfig().getDbUrl(), enviromentService.getConfig().getDbUser());

                OracleDriver dr = new OracleDriver();
                Properties prop = new Properties();
                prop.setProperty("user", enviromentService.getConfig().getDbUser());
                prop.setProperty("password", enviromentService.getConfig().getDbPassword());
                prop.setProperty("defaultTimezone", "UTC");

                oracleConnection = (OracleConnection) dr.connect(enviromentService.getConfig().getJdbcUrl(), prop);

                needDownload = false;

            } catch (SQLException e) {
                log.warn("attempt #" + tryCount + " failed, waiting " + waitTime + " seconds : \n" + e.getMessage());

                lastException = e;
                tryCount++;
                try {
                    TimeUnit.SECONDS.sleep(waitTime);
                    waitTime = waitTime * 2;
                } catch (InterruptedException e1) {
                    log.error("Exception on getOracleConnection", e);
                }
            }
        }

        if (oracleConnection == null) {
            throw new CriticalStopException("Can't get DB connection:", lastException);
        }

        return oracleConnection;
    }

    public void registerDatabaseChangeNotification() throws CriticalStopException {

        log.info("Try registerDatabaseChangeNotification..");

        Properties prop = new Properties();

        // if connected through the VPN, you need to provide the TCP address of the client.
        // For example:
        // prop.setProperty(OracleConnection.NTF_LOCAL_HOST, "10.11.0.6");

        // Ask the server to send the ROWIDs as part of the DCN events (small performance cost):
        prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");

        // Set the DCN_QUERY_CHANGE_NOTIFICATION option for query registration with finer granularity.
        prop.setProperty(OracleConnection.DCN_QUERY_CHANGE_NOTIFICATION, "true");

        prop.setProperty(OracleConnection.DCN_BEST_EFFORT, "true");

        OracleConnection oracleConnection = getOracleConnection();

        // The following operation does a roundtrip to the database to create a new
        // registration for DCN. It sends the client address (ip address and port) that
        // the server will use to connect to the client and send the notification
        // when necessary. Note that for now the registration is empty (we haven't
        // registered
        // any table). This also opens a new thread in the drivers. This thread will be
        // dedicated to DCN (accept connection to the server and dispatch the events to
        // the listeners).
        DatabaseChangeRegistration dcr;
        try {
            dcr = oracleConnection.registerDatabaseChangeNotification(prop);

            try {

                // add the listenerr:
                dcr.addListener(this);

                // for (String sql : enviromentService.getSqlList()) {
                // second step: add objects in the registration:
                Statement stmt = oracleConnection.createStatement();
                // associate the statement with the registration:
                ((OracleStatement) stmt).setDatabaseChangeRegistration(dcr);

                String sql_tlgsend_ocn = "select * from tlg_send_gc_tb where (tlg_status = 0 or tlg_status = 4) and row_state = 0 order by tlg_date asc";
                ResultSet rs = stmt.executeQuery(sql_tlgsend_ocn);

                rs.close();
                stmt.close();

                String[] tableNames = dcr.getTables();
                for (int i = 0; i < tableNames.length; i++) {
                    log.info(tableNames[i] + " is part of the registration.");
                }

                log.info("DBChangeNotification success created");

            } catch (SQLException ex) {
                // if an exception occurs, we need to close the registration in order
                // to interrupt the thread otherwise it will be hanging around.
                if (oracleConnection != null)
                    oracleConnection.unregisterDatabaseChangeNotification(dcr);
                throw ex;
            }
        } catch (SQLException e) {
            throw new CriticalStopException(e);
        }
    }

    @Override
    public void unregisterAllDatabaseChangeNotification() throws CriticalStopException {

        log.info("Try unregister previous DatabaseChangeNotification");

        String unregisterQuery = "select regid, callback from USER_CHANGE_NOTIFICATION_REGS";
        try (OracleConnection oraConn = getOracleConnection();
             Statement stmt = oraConn.createStatement();
             ResultSet rs = stmt.executeQuery(unregisterQuery);) {

            while (rs.next()) {
                long regid = rs.getLong(1);
                String callback = rs.getString(2);
                log.info("unregister {} {}", regid, callback);
                ((OracleConnection) stmt.getConnection()).unregisterDatabaseChangeNotification(regid, callback);
            }
        }
        catch (SQLException e) {
            throw new CriticalStopException(e);
        }
    }


    // так можно убрать один DatabaseChangeNotification
	/*
	// At the end: close the registration (comment out these 3 lines in order
	// to leave the registration open).
	OracleConnection conn3 = connect();
	conn3.unregisterDatabaseChangeNotification(dcr);
	conn3.close();
	*/



    // следим за имзенениями
    @Override
    public void startListener() throws CriticalStopException {

        unregisterAllDatabaseChangeNotification();
        registerDatabaseChangeNotification();

    }

    @Override
    public void onDatabaseChangeNotification(DatabaseChangeEvent event) {

        log.info("Listener: got an event ({} running on thread {})", this, Thread.currentThread());
        log.info(event.toString());

        QueryChangeDescription[] qc = event.getQueryChangeDescription();
        for (int q = 0; q < qc.length; q++) {
            TableChangeDescription[] tc = qc[q].getTableChangeDescription();
            for (int i = 0; i < tc.length; i++) {
                RowChangeDescription[] rcds = tc[i].getRowChangeDescription();
                for (int j = 0; j < rcds.length; j++) {

                    String operation = rcds[j].getRowOperation().name();
                    if(operation.equals("DELETE")) {
                        log.info("row {} was removed - not send", rcds[j].getRowid().stringValue());
                        continue;
                    }

                    if(operation.equals("UPDATE")) {
                        log.info("row {} was updated - not send", rcds[j].getRowid().stringValue());
                        continue;
                    }

                    taskPoolService.addTask(rcds[j].getRowid().stringValue(), null);
               }
            }
        }

        //TODO зачем это тут???
//		synchronized (sendService) {
//			sendService.notify();
//		}
    }
}