package com.rabbitMQ.receptor.receptorRabbit;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@PropertySource("bootstrap.properties")
@ComponentScan(basePackages = {"com.rabbitMQ.receptor.receptorRabbit"})
public class ConfRabbitReceptor {	    	
	
	@Value("${config.repository}")
	public String valueRepo;
	
	@Bean
	Constantes init() {
		Constantes constan = new Constantes();
		constan.setValueRepo(valueRepo);
		Properties p = new Properties();
		try {
			p.load(new FileReader(valueRepo));
			constan.setQUEUE_NAME(p.getProperty(Constantes.queue_name_tag));
			constan.setEXCHANGE_NAME(p.getProperty(Constantes.exchange_name_tag));
			constan.setROUTING_KEY(p.getProperty(Constantes.routing_key_tag));
			String durableString = p.getProperty(Constantes.durable_queue_tag);
			Boolean durable = durableString.trim().equalsIgnoreCase("true")?true:false;
			constan.setIS_DURABLE_QUEUE(durable);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		return constan;
	}
	
    @Bean
    Queue queue(Constantes constan) {    	
        return new Queue(constan.getQUEUE_NAME(), constan.isIS_DURABLE_QUEUE());
    }
    
    @Bean
    TopicExchange exchange(Constantes constan) {
        return new TopicExchange(constan.getEXCHANGE_NAME());
    }
    
    @Bean
    Binding binding(Constantes constan,Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(constan.getROUTING_KEY());
    }

    @Bean
    SimpleMessageListenerContainer container(Constantes constan,ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
	    final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	    container.setConnectionFactory(connectionFactory);
	    container.setQueueNames(constan.getQUEUE_NAME());
	    container.setMessageListener(listenerAdapter);
	    return container;
	}
    
    @Bean
    Receiver receiver() {
        return new Receiver();
    }
 
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, Receiver.RECEIVE_METHOD_NAME);
    }
}
