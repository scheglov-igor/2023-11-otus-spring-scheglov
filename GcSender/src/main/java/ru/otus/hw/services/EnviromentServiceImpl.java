package ru.otus.hw.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
@Data
public class EnviromentServiceImpl implements EnviromentService {

	private final Config config;

	private String hostInfo;

	
	public String getHostInfo() {
		if(hostInfo == null) {
			try {
				InetAddress inetAddress = InetAddress.getLocalHost();
				hostInfo = inetAddress.getHostName() + " (" + inetAddress.getHostAddress() + ")";
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
		}
		return hostInfo;
	}
	

	@Override
	public void showConfig() {


		printVersion();

		System.out.println("##############################################");
		System.out.println("###               configuration            ###");
		System.out.println("##############################################");
		System.out.println("gcUrl =\t\t\t\t" + config.getGcUrl());
		System.out.println("gcUrlAlt =\t\t\t" + getConfig().getGcUrlAlt());
		System.out.println("gcLogin =\t\t\t" + getConfig().getGcLogin());
		System.out.println("gcPassword =\t\t\t" + getConfig().getGcPassword());
		System.out.println("dbType =\t\t\t" + (getConfig().getPostgresVersion() ? "PostgreSql" : "Oracle"));
		System.out.println("postgresVersion =\t\t" + getConfig().getPostgresVersion());
		System.out.println("dbUrl =\t\t\t\t" + getConfig().getDbUrl());
		System.out.println("dbServiceName =\t\t\t" + getConfig().getDbServiceName());
		System.out.println("dbUser =\t\t\t" + getConfig().getDbUser());
//			System.out.println("isAppServer =\t\t\t" + getConfig().getIsAppServer());
//			System.out.println("srvUrl1 =\t\t\t" + getConfig().getSrvUrls());

		System.out.println("##############################################");
		System.out.println("###      common settings with defaults     ###");
		System.out.println("##############################################");
//			System.out.println("useZip =\t\t\t" + getConfig().getUseZip());
		System.out.println("testGcData =\t\t\t" + getConfig().getTestGcData());
		System.out.println("dbPort =\t\t\t" + getConfig().getDbPort());
		System.out.println("dbPassword =\t\t\t" + getConfig().getDbPassword());
//			System.out.println("proxyUrl =\t\t\t" + getConfig().getProxyUrl());
//			System.out.println("proxyPort =\t\t\t" + getConfig().getProxyPort());

//			System.out.println("maxDeltaTimeMinut =\t\t" + getConfig().getMaxDeltaTimeMinut());
//			System.out.println("minDeltaTimeSuccessSec =\t" + getConfig().getMinDeltaTimeSuccessSec());
//			System.out.println("logFolder =\t\t\t" + getConfig().getLogFolder());
//			System.out.println("tmpFolder =\t\t\t" + getConfig().getTmpFolder());
//			System.out.println("exchangeFolder =\t\t" + getConfig().getExchangeFolder());
//			System.out.println("exchangeGcFolder =\t\t" + getConfig().getExchangeGcFolder());
//			System.out.println("exchangeANIFolder =\t\t" + getConfig().getExchangeANIFolder());
//			System.out.println("gcCsvScript =\t\t\t" + getConfig().getGcCsvScript());

//			System.out.println("dbTnsname =\t\t\t" + getConfig().getDbTnsname());
//			System.out.println("fileOwner =\t\t\t" + getConfig().getFileOwner());
		System.out.println("debug =\t\t\t\t" + getConfig().getDebug());
//			System.out.println("checkDiff =\t\t\t" + getConfig().getCheckDiff());
//			System.out.println("runScriptsOnServerOnly =\t" + getConfig().getRunScriptsOnServerOnly());	//TODO нет в конфиге такого поля. И не надо?

//			System.out.println("##############################################");
//			System.out.println("### settings for application server config ###");
//			System.out.println("##############################################");
//			System.out.println("srvUser =\t\t\t" + getConfig().getSrvUser());
//			System.out.println("srvPassword =\t\t\t" + getConfig().getSrvPassword());
//			System.out.println("srvPort =\t\t\t" + getConfig().getSrvPort());
//			System.out.println("srvGcFolder =\t\t\t" + getConfig().getSrvGcFolder());

		System.out.println("noCheckCertificate =\t\t" + getConfig().getNoCheckCertificate());

	}

	//TODO
	public String getVersion() {
		String resourceName = "version.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
		    props.load(resourceStream);
		    String version = (String) props.get("version");
		    return version;
		    
		} catch (IOException e) {
			log.error("Exception on showVersion", e);
			return "unknown";
		}
	}

	private void printVersion() {	
	    String version = getVersion();
	    System.out.println("##############################################");
	    System.out.println("###             GcSender v. " + version + "          ###");
	    System.out.println("##############################################");
	    System.out.println("run on " + getHostInfo());
	}

	//TODO не работает!!!
	@Override
	public void showVersion() {	
	    String version = getVersion();
	    log.info("##############################################");
	    log.info("###             GcSender v. " + version + "          ###");
	    log.info("##############################################");
		log.info("run on {}", getHostInfo());
	}

	@Override
	public int getJavaVersion() {
		String version = System.getProperty("java.version");
		if(version.startsWith("1.")) {
			version = version.substring(2, 3);
		} else {
			int dot = version.indexOf(".");
			if(dot != -1) { version = version.substring(0, dot); }
		} return Integer.parseInt(version);
	}


}
