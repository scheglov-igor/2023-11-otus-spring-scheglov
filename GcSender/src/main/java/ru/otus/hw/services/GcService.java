package ru.otus.hw.services;


import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import ru.otus.hw.exception.CriticalStopException;
import ru.otus.hw.models.GcSendXmlTb;
import ru.otus.hw.models.TlgSendGcTb;
import ru.otus.hw.pojo.Result;
import ru.otus.hw.pojo.SendParams;
import ru.otus.hw.types.Structure;

public interface GcService {

	String convertSOAPMessageToString(SOAPMessage soapMessage);

	String getServiceGcUrl(Structure.ServiceType serviceType) throws CriticalStopException;

	SOAPConnection getSoapConnection() throws UnsupportedOperationException, SOAPException;

//	SOAPMessage createSOAPRequest(FunctionType gcFunction, String tlgText) throws CriticalStopException;

	SOAPMessage createRequest(TlgSendGcTb tlgSendGcTb, SendParams sendParams, GcSendXmlTb gcSendXmlTb)
			throws CriticalStopException;

	Result callSoapResponse(GcSendXmlTb gcSendXmlTb, SOAPMessage soapRequest, SendParams sendParams)
			throws CriticalStopException;

}
