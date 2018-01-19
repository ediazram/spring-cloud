package com.rabbitMQ.receptor.receptorRabbit;

public class Constantes{

	
	public String valueRepo;
	
	public String EXCHANGE_NAME;
    public String ROUTING_KEY;
    public String QUEUE_NAME;
    public boolean IS_DURABLE_QUEUE;
    
    public static final String exchange_name_tag = "rabbit.exchange.name";
    public static final String routing_key_tag = "rabbit.routing.key";
    public static final String queue_name_tag = "rabbit.queue.name";
    public static final String durable_queue_tag="rabbit.queue.durable";
	
	
	public String getValueRepo() {
		return valueRepo;
	}
	public void setValueRepo(String valueRepo) {
		this.valueRepo = valueRepo;
	}
	public String getEXCHANGE_NAME() {
		return EXCHANGE_NAME;
	}
	public void setEXCHANGE_NAME(String eXCHANGE_NAME) {
		EXCHANGE_NAME = eXCHANGE_NAME;
	}
	public String getROUTING_KEY() {
		return ROUTING_KEY;
	}
	public void setROUTING_KEY(String rOUTING_KEY) {
		ROUTING_KEY = rOUTING_KEY;
	}
	public String getQUEUE_NAME() {
		return QUEUE_NAME;
	}
	public void setQUEUE_NAME(String qUEUE_NAME) {
		QUEUE_NAME = qUEUE_NAME;
	}
	public boolean isIS_DURABLE_QUEUE() {
		return IS_DURABLE_QUEUE;
	}
	public void setIS_DURABLE_QUEUE(boolean iS_DURABLE_QUEUE) {
		IS_DURABLE_QUEUE = iS_DURABLE_QUEUE;
	}
	
	
	
}
