package ru.otus.hw.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import ru.otus.hw.exception.CriticalStopException;
import ru.otus.hw.models.GcSendXmlTb;
import ru.otus.hw.models.TlgSendGcTb;
import ru.otus.hw.pojo.Result;
import ru.otus.hw.pojo.SendParams;
import ru.otus.hw.types.Structure;

@Slf4j
@Service
@RequiredArgsConstructor
public class GcServiceImpl implements GcService {

	private final EnviromentService enviromentService;

	private final GcSendXmlTbService gcSendXmlTbService;

	private final String GC_NAMESPACE = "http://SOME/WSERVICE";

	
	@Override
	public SOAPMessage createRequest(TlgSendGcTb tlgSendGcTb, SendParams sendParams, GcSendXmlTb gcSendXmlTb) throws CriticalStopException {

		log.info("createSOAPRequest {}", sendParams.getGcFunction());
		
		try {
			//TODO проверка getTlgText !!!
//			soapRequest = createSOAPRequest(sendParams.getGcFunction(), tlgSendGcTb.getTlgText());
//			public SOAPMessage createSOAPRequest(FunctionType gcFunction, String textNode) throws CriticalStopException {
			
//			try {

//				String namespace = "http://GKOVD/CBD2";

				//Unable to create message factory for SOAP: Unable to create SAAJ meta-factory: Provider com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl not found
				
				MessageFactory messageFactory = MessageFactory.newInstance();
				SOAPMessage soapMessage = messageFactory.createMessage();
				MimeHeaders headers = soapMessage.getMimeHeaders();
				// TODO
				headers.addHeader("SOAPAction", GC_NAMESPACE + "/" + sendParams.getGcFunction());
				// TODO
				String loginPassword = enviromentService.getConfig().getGcLogin() + ":" + enviromentService.getConfig().getGcPassword();
				
				// headers.addHeader("Authorization", "Basic " + new String(Base64.encode(loginPassword.getBytes())));
				headers.addHeader("Authorization", "Basic " + new String(Base64.encodeBase64(loginPassword.getBytes())));

				SOAPPart soapPart = soapMessage.getSOAPPart();

				// SOAP Envelope
				SOAPEnvelope envelope = soapPart.getEnvelope();
				// envelope.addNamespaceDeclaration("cbd2", namespace);
				envelope.addNamespaceDeclaration("", GC_NAMESPACE);

				if (enviromentService.getConfig().getTestGcData()) {
					SOAPHeader soapHeader = envelope.getHeader();
					SOAPElement soapHeaderElem = soapHeader.addChildElement("UserClaims", "");
					SOAPElement soapHeaderElem1 = soapHeaderElem.addChildElement("zc_id", "");
					soapHeaderElem1.addTextNode("11");
				}

				// SOAP Body
				SOAPBody soapBody = envelope.getBody();

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);

				Document document = null;

				String textNode = tlgSendGcTb.getTlgText();
				
				if((textNode == null) || textNode.isEmpty()) {
					log.error("STOP! tlg_text is empty!");
					throw new CriticalStopException("tlg_text is empty!");
				}
				
				InputStream is = new ByteArrayInputStream(textNode.getBytes());
				DocumentBuilder db = dbf.newDocumentBuilder();
				document = db.parse(is);

				soapBody.addDocument(document);

				soapMessage.saveChanges();

				//DEBUG
				log.info("soapRequest = " + convertSOAPMessageToString(soapMessage));

				return soapMessage;

			
						
		} catch (Exception e) {
			gcSendXmlTb.setResult(new Result(e.getMessage(), null, null, null, sendParams).toString());
			gcSendXmlTb.setStatus(4);
			
			log.error("Exception on createRequest", e);
			throw new CriticalStopException(e);
		}
	}
	
	/**
	 * Method used to creatnamespacee the SOAP Request
	 * 
	 * @throws IOException
	 * @throws SQLException
	 * @throws UnsupportedOperationException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws CriticalStopException
	 */
//	@Override
//	public SOAPMessage createSOAPRequest(FunctionType gcFunction, String textNode) throws CriticalStopException {
//
//		
//	}

	// TODO
	/*
	SOAPElement soapBodyElem = soapBody.addChildElement("SendAgreedSegment", "cbd2");		
	////////////////////		
	SOAPElement soapBodyElem1;
	SOAPElement soapBodyElem2;
	SOAPElement soapBodyElem3;		
	soapBodyElem1 = soapBodyElem.addChildElement("id", "cbd2");
	soapBodyElem1.addTextNode("-1"); // 691942
	soapBodyElem2 = soapBodyElem.addChildElement("airwayCode", "cbd2");
	soapBodyElem2.addTextNode(sendAgreedSegments.getAirwayCode());
	soapBodyElem3 = soapBodyElem.addChildElement("atsBeginPoint", "cbd2");
	soapBodyElem3.addTextNode(sendAgreedSegments.getAtsBeginPoint());
	SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("atsEndPoint", "cbd2");
	soapBodyElem4.addTextNode(sendAgreedSegments.getAtsEndPoint());
	SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("isOneWay", "cbd2");
	soapBodyElem5.addTextNode(sendAgreedSegments.getIsOneWay().toString());
	SOAPElement soapBodyElem6 = soapBodyElem.addChildElement("levelFrom", "cbd2");
	soapBodyElem6.addTextNode(sendAgreedSegments.getLevelFrom().toString());
	SOAPElement soapBodyElem7 = soapBodyElem.addChildElement("levelTo", "cbd2");
	soapBodyElem7.addTextNode(sendAgreedSegments.getLevelTo().toString());
	SOAPElement soapBodyElem8 = soapBodyElem.addChildElement("dateFrom", "cbd2");
	soapBodyElem8.addTextNode(sendAgreedSegments.getDateFromStrZ());
	SOAPElement soapBodyElem9 = soapBodyElem.addChildElement("dateTo", "cbd2");
	soapBodyElem9.addTextNode(sendAgreedSegments.getDateToStrZ());
	*/
	

	@Override
	public Result callSoapResponse(GcSendXmlTb gcSendXmlTb, SOAPMessage soapRequest, SendParams sendParams) throws CriticalStopException {

		gcSendXmlTb.setRequestXml(convertSOAPMessageToString(soapRequest));
		
		log.info("### callSoapResponse...");
		try {
			
			String serviceGcUrl = getServiceGcUrl(sendParams.getGcSOAPService());
			SOAPConnection soapConnection = getSoapConnection();
			SOAPMessage soapResponse = null;
			SOAPException se = null;
	
			int tryCount = 1;
			boolean needDownload = true;
			while (needDownload && (tryCount <= 2)) {
				try {
					log.info("sending to {}...", serviceGcUrl);
					//soapResponse = soapConnection.call(soapRequest, serviceGcUrl);
					
					URL endpoint = createEndpoint(serviceGcUrl);
					
					soapResponse = soapConnection.call(soapRequest, endpoint);
					
					needDownload = false;
				} catch (SOAPException e) {
					
					int waitTime = tryCount * tryCount;
					String fault = "attempt #" + tryCount + " failed, waiting " + waitTime + " seconds : \n" + e.getMessage(); 
					log.warn(fault);
					
					Result r = new Result(fault, 0, null, fault, sendParams);
					gcSendXmlTb.setResult(r.toString());
					
					se = e;
					tryCount++;
					TimeUnit.SECONDS.sleep(waitTime);

					gcSendXmlTbService.save(gcSendXmlTb);
				}
			}
			if (soapResponse == null) {
				throw new CriticalStopException("STOP! Can't send message: ", se);
			}
	
			SOAPBody responseSoapBody = soapResponse.getSOAPBody();
	
			//DEBUG
			log.info("response {}", convertSOAPMessageToString(soapResponse));
			
			gcSendXmlTb.setResponseXml(convertSOAPMessageToString(soapResponse));
	
			Result result = new Result(responseSoapBody, sendParams);

			log.info("result {}", result);
			log.info("status {}", result.getStatusStr());
			
			gcSendXmlTb.setResult(result.toString());

			return result;
		}
		catch (UnsupportedOperationException | InterruptedException | IOException | SOAPException e ) {
			log.error("Exception on callSoapResponse", e);
			gcSendXmlTb.setResult(new Result(e.getMessage(), null, null, null, sendParams).toString());
			gcSendXmlTb.setStatus(4);
			throw new CriticalStopException(e);
		}
		
		catch (CriticalStopException e) {
			log.error("CriticalStopException on callSoapResponse", e);
			gcSendXmlTb.setResult(new Result(e.getMessage(), null, null, null, sendParams).toString());
			gcSendXmlTb.setStatus(4);
			throw new CriticalStopException(e);
		}
	}
	

	@Override
	public SOAPConnection getSoapConnection() throws UnsupportedOperationException, SOAPException {

		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		return soapConnection;
	}

	
	@SuppressWarnings("deprecation")
	public URL createEndpoint(String url) throws MalformedURLException {
		return new URL(new URL(GC_NAMESPACE), url,
		        new URLStreamHandler() {
		            @Override
		            protected URLConnection openConnection(URL url) throws IOException {
		              
		                URL target = new URL(url.toString());
		                
		                URLConnection connection = target.openConnection();
		                
		             	// если с 5,57 - без проверки сертификатов
		                try {
							if(enviromentService.getConfig().getNoCheckCertificate()) {
								try {
									connection = getNoCertConnection(url.toString());
								} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
									log.error("Exception on openConnection", e);
									throw new RuntimeException(e);
								}
							}
						} catch (CriticalStopException e) {
							log.error("CriticalStopException on openConnection", e);
						}			                
		                
			            // Connection settings
			            connection.setConnectTimeout(10000); // 10 sec
			            //TODO выбрать правильно время. Скорее около 3-4 мин.
			            connection.setReadTimeout(60000); // 1 min
			            return(connection);
			        }
		    	}
		    );
	}

	
	private URLConnection getNoCertConnection(String urlStr) throws NoSuchAlgorithmException, KeyManagementException, IOException  {
		  
		  log.warn("Warning! NO CERT loading. FOR DEVELOPMENT PURPOSES ONLY!");
		  
	      // Create a trust manager that does not validate certificate chains
	      TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	              public X509Certificate[] getAcceptedIssuers() {
	                  return null;
	              }
	              public void checkClientTrusted(X509Certificate[] certs, String authType) {
	              }
	              public void checkServerTrusted(X509Certificate[] certs, String authType) {
	              }
	          }
	      };

	      // Install the all-trusting trust manager
	      SSLContext sc = SSLContext.getInstance("SSL");
	      sc.init(null, trustAllCerts, new java.security.SecureRandom());
	      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	      // Create all-trusting host name verifier
	      HostnameVerifier allHostsValid = new HostnameVerifier() {
	          public boolean verify(String hostname, SSLSession session) {
	              return true;
	          }
	      };

	      // Install the all-trusting host verifier
	      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

	      URL url = new URL(urlStr);
	      URLConnection con = url.openConnection();
	      
	      return con;
	      
	}
	

	@Override
	public String getServiceGcUrl(Structure.ServiceType serviceType) throws CriticalStopException {

		// return gcUrl + "/CBD2/1.0" + testGcData + "/" + serviceType.toString() + ".asmx?WSDL";
		//TODO return enviromentService.getConfig().getGcUrl() + "/CBD2/1.1" + (enviromentService.getConfig().getTestGcData() ? "/test/" : "/") + serviceType + ".asmx?WSDL";

		return "http://localhost:8088/mockAgreedRoutesSoap.asmx?WSDL";


	}

	@Override
	public String convertSOAPMessageToString(SOAPMessage soapMessage) {
		String message = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			soapMessage.writeTo(baos);
			message = baos.toString();
		} catch (SOAPException | IOException e) {
			log.error("Exception on convertSOAPMessageToString", e);
			
		}
		return message;
	}

	public static SOAPMessage convertStringToSOAPMessage(String soapString) throws IOException, SOAPException {
		InputStream is = new ByteArrayInputStream(soapString.getBytes());
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, is);
		return soapMessage;
	}

}
