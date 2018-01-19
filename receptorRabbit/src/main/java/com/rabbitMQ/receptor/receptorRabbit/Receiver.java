package com.rabbitMQ.receptor.receptorRabbit;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class Receiver {

	public static final String RECEIVE_METHOD_NAME = "receiveMessage";
	 
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
        
        restTemplate.postForObject("http://localhost:8080/book-service/books/save", book,Book.class);		
    }
}
