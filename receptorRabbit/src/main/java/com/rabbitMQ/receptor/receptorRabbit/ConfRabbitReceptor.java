package com.rabbitMQ.receptor.receptorRabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.rabbitMQ.receptor.receptorRabbit"})
public class ConfRabbitReceptor {	    	
	
	@Value("${" + Constantes.queue_name_tag + "}")
	public String queueName;
	
	@Value("${" + Constantes.exchange_name_tag + "}")
	public String exchangeName;
	
	@Value("${" + Constantes.routing_key_tag + "}")
	public String routingKey;
	
	@Value("${" + Constantes.durable_queue_tag + "}")
	public String durableString;
	
	@Bean
	Constantes init() {
		Constantes constan = new Constantes();
		try {
			constan.setQUEUE_NAME(queueName);
			constan.setEXCHANGE_NAME(exchangeName);
			constan.setROUTING_KEY(routingKey);
			Boolean durable = durableString.trim().equalsIgnoreCase("true") ? true : false;
			constan.setIS_DURABLE_QUEUE(durable);
		} catch (Exception e) {			
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
