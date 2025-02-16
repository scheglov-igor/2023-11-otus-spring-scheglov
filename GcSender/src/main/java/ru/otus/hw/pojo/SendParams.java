package ru.otus.hw.pojo;


import ru.otus.hw.types.Structure;

public class SendParams {
	
	private Structure.ServiceType gcSOAPService;
	private Structure.FunctionType gcFunction;
		
	public SendParams(Structure.ServiceType gcSOAPService, Structure.FunctionType gcFunction) {
		super();
		this.gcSOAPService = gcSOAPService;
		this.gcFunction = gcFunction;
	}
	
	public Structure.FunctionType getGcFunction() {
		return gcFunction;
	}
	public void setGcFunction(Structure.FunctionType gcFunction) {
		this.gcFunction = gcFunction;
	}
	public Structure.ServiceType getGcSOAPService() {
		return gcSOAPService;
	}
	public void setGcSOAPService(Structure.ServiceType gcSOAPService) {
		this.gcSOAPService = gcSOAPService;
	}
	
	
	
}
