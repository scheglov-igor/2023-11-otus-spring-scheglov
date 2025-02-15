package ru.otus.hw.services;

import java.sql.SQLException;

import jakarta.xml.soap.SOAPMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.CriticalStopException;
import ru.otus.hw.exception.ManualStopException;
import ru.otus.hw.models.GcSendXmlTb;
import ru.otus.hw.models.TlgSendGcTb;
import ru.otus.hw.pojo.Result;
import ru.otus.hw.pojo.SendParams;
import ru.otus.hw.pojo.Status;
import ru.otus.hw.types.Structure;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendServiceImpl implements SendService {
	
	private final TlgSendGcTbService tlgSendGcTbService;
	private final GcSendXmlTbService gcSendXmlTbService;
	private final GcService gcService;
	private final QueueService queueService;


	// а очередь будет вызывать отдельный метод, который будет отправлять. 
	@Override
	public String sendByRowid(String rowid, TlgSendGcTb tlgSendGcTb) {

		log.info("-------------------------");
		log.info("starting sendByRowid id={}", rowid);
		
		try {
			
			if(tlgSendGcTb == null) {
				tlgSendGcTb = tlgSendGcTbService.findByRowId(rowid).orElseThrow(
						() -> new CriticalStopException("no row with rowid " + rowid));
			}

			send(tlgSendGcTb);

		} catch (CriticalStopException e) {
			log.error("Exception on sendByRowid", e);
		} catch (Exception e) {
			log.error("Exception on sendById", e);
		}

		finally {
			// в любом случае убираем из SET - если успешно или если была ошибка.
			queueService.removeRowid(tlgSendGcTb.getRowid());
			log.info("-------------------------");
		}
		
		return rowid;
	}

	
	// а очередь будет вызывать отдельный метод, который будет отправлять. 
	@Override
	public Long sendById(Long id, TlgSendGcTb tlgSendGcTb) {

		log.info("-------------------------");
		log.info("starting sendById id={}", id);
		
		try {
			
			if(tlgSendGcTb == null) {
				tlgSendGcTb = tlgSendGcTbService.findById(id).orElseThrow(
						() -> new CriticalStopException("no row with id " + id));
			}

			send(tlgSendGcTb);

		} catch (CriticalStopException e) {
			log.error("Exception on sendById", e);
		} catch (Exception e) {
			log.error("Exception on sendById", e);
		}

		finally {
			// в любом случае убираем из SET - если успешно или если была ошибка.
			queueService.removeId(tlgSendGcTb.getId());
			log.info("-------------------------");
		}
		
		return id;
	}


	private TlgSendGcTb send(TlgSendGcTb tlgSendGcTb) {

		SendParams sendParams = initSendParams(tlgSendGcTb);

		GcSendXmlTb gcSendXmlTb = new GcSendXmlTb(tlgSendGcTb, sendParams);

		Result sendingResult = null;

		try {
			checkSendingToGc(tlgSendGcTb);

			SOAPMessage soapRequest = gcService.createRequest(tlgSendGcTb, sendParams, gcSendXmlTb);

			sendingResult = gcService.callSoapResponse(gcSendXmlTb, soapRequest, sendParams);

		} catch (ManualStopException e) {
			log.info("ManualStop on send");
		} catch (Exception e) {
			log.error("Exception on send", e);
			tlgSendGcTb.setGcSasErrorMessage(e.getMessage());
		}
		finally {

			tlgSendGcTb = fillTlgSendGcResult(tlgSendGcTb, sendingResult);
			gcSendXmlTb = fillGcSendXmlTbResult(gcSendXmlTb, sendingResult);

			log.info("gcSendXmlTb = {}", gcSendXmlTb.toShortString());
			log.info("tlgSendGcTb = {}", tlgSendGcTb);

			gcSendXmlTbService.save(gcSendXmlTb);
			tlgSendGcTbService.save(tlgSendGcTb);

		}

		return tlgSendGcTb;
	}
	
	public SendParams initSendParams(TlgSendGcTb tlgSendGcTb) {
		
		switch (tlgSendGcTb.getTlgWebservice().toUpperCase()) {
		case "SENDAGREEDSEGMENT":
			log.info("SendAgreedSegment() in AgreedRoutes.asmx");
			return new SendParams(Structure.ServiceType.AgreedRoutes, Structure.FunctionType.SendAgreedSegment);
	
		case "DELETEAGREEDSEGMENT":
			log.info("DeleteAgreedSegment() in AgreedRoutes.asmx");
			return new SendParams(Structure.ServiceType.AgreedRoutes, Structure.FunctionType.DeleteAgreedSegment);
			
		case "SENDACTIVERSTRAREA":
			log.info("SendActiveRstrArea() in AgreedRoutes.asmx");
			return new SendParams(Structure.ServiceType.AgreedRoutes, Structure.FunctionType.SendActiveRstrArea);
			
		case "DELETEACTIVERSTRAREA":
			log.info("DeleteActiveRstrArea() in AgreedRoutes.asmx");
			return new SendParams(Structure.ServiceType.AgreedRoutes, Structure.FunctionType.DeleteActiveRstrArea);
		/*	
		case "SENDTRAFFICREPORT":
			log.info("SendTrafficReport() in TrafficReport.asmx");
			// TODO
			throw new CriticalStopException("STOP!");
		case "SENDTELEGRAM":
			log.info("SendTelegram() in Telegrams.asmx");
			// TODO 
			throw new CriticalStopException("STOP!");
		*/
		default:
			log.info("Unknown GC WebService! {}", tlgSendGcTb.getTlgWebservice());
			throw null;
		}
	}


	
	public void checkSendingToGc(TlgSendGcTb tlgSendGcTb) throws CriticalStopException, ManualStopException {
		
		if (tlgSendGcTb == null) {
			throw new CriticalStopException("STOP sending. Row not found!");
		}
		
		if (tlgSendGcTb.getRowState() == null || !tlgSendGcTb.getRowState().equals(0)) {
			log.info("STOP by ROW_STATE {}", tlgSendGcTb.getRowState());
			throw new ManualStopException("STOP sending. ROW_STATE " + tlgSendGcTb.getRowState());
		}
		
		// не отправляем, если:
		// нет статуса
		// статус = 1 (не отправлять)
		// статус = 2 (успешно)
		// статус = 6 (отправлено, вернулась ошибка)
		// то есть остается 0 - нужно отправить или 4 - ошибка при отправке
		if (tlgSendGcTb.getTlgStatus() == null ||
			tlgSendGcTb.getTlgStatus().equals(new Status(1)) ||
			tlgSendGcTb.getTlgStatus().equals(new Status(2)) ||
			tlgSendGcTb.getTlgStatus().equals(new Status(6)) ) {
			log.info("STOP by TLG_STATUS {}", tlgSendGcTb.getTlgStatus());
			throw new ManualStopException("STOP sending. TLG_STATUS " + tlgSendGcTb.getTlgStatus());
		}
		
		if (tlgSendGcTb.getTlgWebservice() == null) {
			log.info("STOP. No GC WebService");
			throw new ManualStopException("STOP. No GC WebService");
		}
	}
	
	
	
	
	
	


	public TlgSendGcTb fillTlgSendGcResult(TlgSendGcTb tlgSendGcTb, Result sendingResult) {

		log.info("sendingResult = {}", sendingResult);

		Long gcSasId = sendingResult.getId();
		if((tlgSendGcTb.getGcSasId() == null) || (gcSasId != null)) {
			tlgSendGcTb.setGcSasId(gcSasId);
		}

		if (sendingResult.getErrorCode() != null) {

			Integer errorId = sendingResult.getErrorCode();
			tlgSendGcTb.setGcSasErrorcode(errorId);
		}

		if (sendingResult.getFullErrorMessage().length() > 0) {
			tlgSendGcTb.setGcSasErrorMessage(sendingResult.getFullErrorMessage());
		}

		tlgSendGcTb.setTlgStatus(sendingResult.getStatus());
		tlgSendGcTb.setTlgResult(sendingResult.createTlgResult());

		return tlgSendGcTb;
	}


	public GcSendXmlTb fillGcSendXmlTbResult(GcSendXmlTb gcSendXmlTb, Result sendingResult) {
		gcSendXmlTb.setStatus(sendingResult.getStatus());
		gcSendXmlTb.setResult(sendingResult.toString());
		return gcSendXmlTb;
	}

}
