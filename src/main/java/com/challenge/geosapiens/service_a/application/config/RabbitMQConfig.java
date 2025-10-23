package com.challenge.geosapiens.service_a.application.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String DELIVERY_PERSON_EXCHANGE = "delivery-person.exchange";

    public static final String ORDER_CREATE_QUEUE = "order.create.queue";
    public static final String ORDER_UPDATE_QUEUE = "order.update.queue";
    public static final String ORDER_DELETE_QUEUE = "order.delete.queue";

    public static final String USER_CREATE_QUEUE = "user.create.queue";
    public static final String USER_UPDATE_QUEUE = "user.update.queue";
    public static final String USER_DELETE_QUEUE = "user.delete.queue";

    public static final String DELIVERY_PERSON_CREATE_QUEUE = "delivery-person.create.queue";
    public static final String DELIVERY_PERSON_UPDATE_QUEUE = "delivery-person.update.queue";
    public static final String DELIVERY_PERSON_DELETE_QUEUE = "delivery-person.delete.queue";

    public static final String ORDER_CREATE_ROUTING_KEY = "order.create";
    public static final String ORDER_UPDATE_ROUTING_KEY = "order.update";
    public static final String ORDER_DELETE_ROUTING_KEY = "order.delete";

    public static final String USER_CREATE_ROUTING_KEY = "user.create";
    public static final String USER_UPDATE_ROUTING_KEY = "user.update";
    public static final String USER_DELETE_ROUTING_KEY = "user.delete";

    public static final String DELIVERY_PERSON_CREATE_ROUTING_KEY = "delivery-person.create";
    public static final String DELIVERY_PERSON_UPDATE_ROUTING_KEY = "delivery-person.update";
    public static final String DELIVERY_PERSON_DELETE_ROUTING_KEY = "delivery-person.delete";

    public static final String ORDER_DLQ = "order.dlq";
    public static final String USER_DLQ = "user.dlq";
    public static final String DELIVERY_PERSON_DLQ = "delivery-person.dlq";

    public static final String ORDER_DLX = "order.dlx";
    public static final String USER_DLX = "user.dlx";
    public static final String DELIVERY_PERSON_DLX = "delivery-person.dlx";

    public static final String ORDER_RETRY_QUEUE = "order.retry.queue";
    public static final String USER_RETRY_QUEUE = "user.retry.queue";
    public static final String DELIVERY_PERSON_RETRY_QUEUE = "delivery-person.retry.queue";

    public static final String ORDER_RETRY_EXCHANGE = "order.retry.exchange";
    public static final String USER_RETRY_EXCHANGE = "user.retry.exchange";
    public static final String DELIVERY_PERSON_RETRY_EXCHANGE = "delivery-person.retry.exchange";

    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final int RETRY_DELAY_MS = 5000; // 5 seconds

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public TopicExchange orderDLX() {
        return new TopicExchange(ORDER_DLX);
    }

    @Bean
    public Queue orderDLQ() {
        return QueueBuilder.durable(ORDER_DLQ).build();
    }

    @Bean
    public Queue orderCreateQueue() {
        return QueueBuilder.durable(ORDER_CREATE_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "order.create.retry")
                .build();
    }

    @Bean
    public Queue orderUpdateQueue() {
        return QueueBuilder.durable(ORDER_UPDATE_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "order.update.retry")
                .build();
    }

    @Bean
    public Queue orderDeleteQueue() {
        return QueueBuilder.durable(ORDER_DELETE_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "order.delete.retry")
                .build();
    }

    @Bean
    public Binding orderCreateBinding() {
        return BindingBuilder.bind(orderCreateQueue()).to(orderExchange()).with(ORDER_CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding orderUpdateBinding() {
        return BindingBuilder.bind(orderUpdateQueue()).to(orderExchange()).with(ORDER_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Binding orderDeleteBinding() {
        return BindingBuilder.bind(orderDeleteQueue()).to(orderExchange()).with(ORDER_DELETE_ROUTING_KEY);
    }

    @Bean
    public Binding orderDLQBinding() {
        return BindingBuilder.bind(orderDLQ()).to(orderDLX()).with("order.#");
    }

    // Order Retry Configuration
    @Bean
    public DirectExchange orderRetryExchange() {
        return new DirectExchange(ORDER_RETRY_EXCHANGE);
    }

    @Bean
    public Queue orderRetryQueue() {
        return QueueBuilder.durable(ORDER_RETRY_QUEUE)
                .withArgument("x-message-ttl", RETRY_DELAY_MS)
                .withArgument("x-dead-letter-exchange", ORDER_EXCHANGE)
                .build();
    }

    @Bean
    public Binding orderRetryBinding() {
        return BindingBuilder.bind(orderRetryQueue()).to(orderRetryExchange()).with("order.#");
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public TopicExchange userDLX() {
        return new TopicExchange(USER_DLX);
    }

    @Bean
    public Queue userDLQ() {
        return QueueBuilder.durable(USER_DLQ).build();
    }

    @Bean
    public Queue userCreateQueue() {
        return QueueBuilder.durable(USER_CREATE_QUEUE)
                .withArgument("x-dead-letter-exchange", USER_RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "user.create.retry")
                .build();
    }

    @Bean
    public Queue userUpdateQueue() {
        return QueueBuilder.durable(USER_UPDATE_QUEUE)
                .withArgument("x-dead-letter-exchange", USER_RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "user.update.retry")
                .build();
    }

    @Bean
    public Queue userDeleteQueue() {
        return QueueBuilder.durable(USER_DELETE_QUEUE)
                .withArgument("x-dead-letter-exchange", USER_RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "user.delete.retry")
                .build();
    }

    @Bean
    public Binding userCreateBinding() {
        return BindingBuilder.bind(userCreateQueue()).to(userExchange()).with(USER_CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding userUpdateBinding() {
        return BindingBuilder.bind(userUpdateQueue()).to(userExchange()).with(USER_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Binding userDeleteBinding() {
        return BindingBuilder.bind(userDeleteQueue()).to(userExchange()).with(USER_DELETE_ROUTING_KEY);
    }

    @Bean
    public Binding userDLQBinding() {
        return BindingBuilder.bind(userDLQ()).to(userDLX()).with("user.#");
    }

    // User Retry Configuration
    @Bean
    public DirectExchange userRetryExchange() {
        return new DirectExchange(USER_RETRY_EXCHANGE);
    }

    @Bean
    public Queue userRetryQueue() {
        return QueueBuilder.durable(USER_RETRY_QUEUE)
                .withArgument("x-message-ttl", RETRY_DELAY_MS)
                .withArgument("x-dead-letter-exchange", USER_EXCHANGE)
                .build();
    }

    @Bean
    public Binding userRetryBinding() {
        return BindingBuilder.bind(userRetryQueue()).to(userRetryExchange()).with("user.#");
    }

    @Bean
    public TopicExchange deliveryPersonExchange() {
        return new TopicExchange(DELIVERY_PERSON_EXCHANGE);
    }

    @Bean
    public TopicExchange deliveryPersonDLX() {
        return new TopicExchange(DELIVERY_PERSON_DLX);
    }

    @Bean
    public Queue deliveryPersonDLQ() {
        return QueueBuilder.durable(DELIVERY_PERSON_DLQ).build();
    }

    @Bean
    public Queue deliveryPersonCreateQueue() {
        return QueueBuilder.durable(DELIVERY_PERSON_CREATE_QUEUE)
                .withArgument("x-dead-letter-exchange", DELIVERY_PERSON_RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "delivery-person.create.retry")
                .build();
    }

    @Bean
    public Queue deliveryPersonUpdateQueue() {
        return QueueBuilder.durable(DELIVERY_PERSON_UPDATE_QUEUE)
                .withArgument("x-dead-letter-exchange", DELIVERY_PERSON_RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "delivery-person.update.retry")
                .build();
    }

    @Bean
    public Queue deliveryPersonDeleteQueue() {
        return QueueBuilder.durable(DELIVERY_PERSON_DELETE_QUEUE)
                .withArgument("x-dead-letter-exchange", DELIVERY_PERSON_RETRY_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "delivery-person.delete.retry")
                .build();
    }

    @Bean
    public Binding deliveryPersonCreateBinding() {
        return BindingBuilder.bind(deliveryPersonCreateQueue()).to(deliveryPersonExchange()).with(DELIVERY_PERSON_CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding deliveryPersonUpdateBinding() {
        return BindingBuilder.bind(deliveryPersonUpdateQueue()).to(deliveryPersonExchange()).with(DELIVERY_PERSON_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Binding deliveryPersonDeleteBinding() {
        return BindingBuilder.bind(deliveryPersonDeleteQueue()).to(deliveryPersonExchange()).with(DELIVERY_PERSON_DELETE_ROUTING_KEY);
    }

    @Bean
    public Binding deliveryPersonDLQBinding() {
        return BindingBuilder.bind(deliveryPersonDLQ()).to(deliveryPersonDLX()).with("delivery-person.#");
    }

    // DeliveryPerson Retry Configuration
    @Bean
    public DirectExchange deliveryPersonRetryExchange() {
        return new DirectExchange(DELIVERY_PERSON_RETRY_EXCHANGE);
    }

    @Bean
    public Queue deliveryPersonRetryQueue() {
        return QueueBuilder.durable(DELIVERY_PERSON_RETRY_QUEUE)
                .withArgument("x-message-ttl", RETRY_DELAY_MS)
                .withArgument("x-dead-letter-exchange", DELIVERY_PERSON_EXCHANGE)
                .build();
    }

    @Bean
    public Binding deliveryPersonRetryBinding() {
        return BindingBuilder.bind(deliveryPersonRetryQueue()).to(deliveryPersonRetryExchange()).with("delivery-person.#");
    }
}
