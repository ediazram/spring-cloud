package com.rabbitMQ.receptor.receptorRabbit;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

public class Receiver {

	public static final String RECEIVE_METHOD_NAME = "receiveMessage";
	 
	@Autowired
    private EurekaClient eurekaClient;
	
	@Value("${service.gateway.serviceId}")
    private String gatewaySearchServiceId;
	
    public void receiveMessage(String message) {
        System.out.println("[Receiver] ha recibido el mensaje en proyecto RECEPTOR \"" + message + '"');
        //ahora debemos almacenarlo en BBDD. Pero para ello (Al recibir un libro), en vez de mapear
        // esa identidad, vamos ha hacer uso de la rest
        RestTemplate restTemplate = new RestTemplate();
        Book book = new Book();
        try {
			JSONObject json = new JSONObject(message);
			book.setAuthor(json.getString("author"));
	        book.setTitle(json.getString("title"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        
        Application application = eurekaClient.getApplication(gatewaySearchServiceId);
        InstanceInfo instanceInfo = application.getInstances().get(0);
        restTemplate.postForObject("http://"+instanceInfo.getHostName()+":"+instanceInfo.getPort()+"/book-service/books/save", book,Book.class);		
    }
}
