package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.pojo.SendParams;
import ru.otus.hw.types.Structure;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gc_send_xml_tb")
public class GcSendXmlTb {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "gc_service")
	private Structure.ServiceType gcService;

	@Column(name = "data_id")
	private Long dataId;

	@Column(name = "request_xml")
	private String requestXml;

	@Column(name = "response_xml")
	private String responseXml;

	/*
	 * 2 - успешно
	 * 4 - ошибка при отправке
	 * 6 - отправили, получили ошибку
	 */
	@Column(name = "status")
	private Integer status;

	@Column(name = "result")
	private String result;

	@Enumerated(EnumType.STRING)
	@Column(name = "gc_function")
	private Structure.FunctionType gcFunction;

	@Column(name = "row_state")
	private Integer rowState;

	@Column(name = "date_change")
	private LocalDateTime dateChange;

	//TODO надо это в дто вынести

	public GcSendXmlTb(TlgSendGcTb tlgSendGcTb, SendParams sendParams) {
		super();
		this.gcService = sendParams.getGcSOAPService();
		this.gcFunction = sendParams.getGcFunction();
		this.dataId = tlgSendGcTb.getId();
		this.rowState = 0;
		this.dateChange = LocalDateTime.now();
	}

	public GcSendXmlTb(Structure.ServiceType gcService, Structure.FunctionType gcFunction, Long dataId) {
		this.gcService = gcService;
		this.gcFunction = gcFunction;
		this.dataId = dataId;
		this.rowState = 0;
		this.dateChange = LocalDateTime.now();
	}

	public void setResult(String resultString) {
		if (resultString != null && resultString.length() > 1000) {
            resultString = resultString.substring(0, 999);
        }
		this.result = resultString;
	}

	@Override
	public String toString() {
		return "GcSendXmlTb [id=" + id + ", rowState=" + rowState + ", dateChange=" + dateChange + ", gcService="
				+ gcService + ", gcFunction=" + gcFunction + ", dataId=" + dataId
				+ ", requestXml=" + (requestXml != null && !requestXml.isEmpty())
				+ ", responseXml=" + (responseXml != null && !responseXml.isEmpty())
				+ ", status=" + status +  ", result=" + result +"]";
	}

	public String toShortString() {
		return "GcSendXmlTb [id=" + id + ", rowState=" + rowState + ", dateChange=" + dateChange +  ", dataId=" + dataId +
			   ", gcService=" + gcService + ", gcFunction=" + gcFunction +
			   ", requestXml=" + (requestXml != null && !requestXml.isEmpty()) +
			   ", responseXml=" + (responseXml != null && !responseXml.isEmpty()) +
			   ", status=" + status + ", result=" + result + "]";
	}


}
