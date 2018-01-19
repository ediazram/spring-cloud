package es.spring.batch.example;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.amqp.AmqpItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    RabbitTemplate rabbitTemplate;
    
    
    protected static final String EXCHANGE_NAME = "exchange_name";
    protected static final String ROUTING_KEY = "routing_key";
 
    protected static final String QUEUE_NAME = "queue_name";
    protected static final boolean IS_DURABLE_QUEUE = false;
    
    
    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, IS_DURABLE_QUEUE);
    }
    
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
    
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
    
    @Bean
    public FlatFileItemReader<Book> reader() {
        FlatFileItemReader<Book> reader = new FlatFileItemReader<Book>();
        reader.setResource(new ClassPathResource("sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<Book>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "author", "title" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Book>() {{
                setTargetType(Book.class);
            }});
        }});
        return reader;
    }

    @Bean
    public BookItemProcessor processor() {
        return new BookItemProcessor();
    }

    @Bean
    public AmqpItemWriter<String> writer() {
    	rabbitTemplate.setExchange(EXCHANGE_NAME);
    	rabbitTemplate.setQueue(QUEUE_NAME);
    	rabbitTemplate.setRoutingKey(ROUTING_KEY);

        return new AmqpItemWriter<String>(rabbitTemplate);
    }

    @Bean
    public Job importBooksJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importBooksJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(paso1())
                .end()
                .build();
    }

    @Bean
    public Step paso1() {
        return stepBuilderFactory.get("paso1")
                .<Book, String> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    
    
    
    // ////////////////////////////////////////////// //
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(BatchConfiguration.QUEUE_NAME);
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
    
    class Receiver {

		public static final String RECEIVE_METHOD_NAME = "receiveMessage";
		 
	    public void receiveMessage(String message) {
	        System.out.println("[Receiver] ha recibido el mensaje \"" + message + '"');
	    }
	}
    // ////////////////////////////////////////////// //
}
