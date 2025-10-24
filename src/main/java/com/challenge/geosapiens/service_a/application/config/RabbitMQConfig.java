package com.challenge.geosapiens.service_a.application.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String USER_EXCHANGE = "user.exchange";

    public static final String ORDER_CREATE_QUEUE = "order.create.queue";
    public static final String ORDER_UPDATE_QUEUE = "order.update.queue";
    public static final String ORDER_DELETE_QUEUE = "order.delete.queue";

    public static final String USER_CREATE_QUEUE = "user.create.queue";
    public static final String USER_UPDATE_QUEUE = "user.update.queue";
    public static final String USER_DELETE_QUEUE = "user.delete.queue";

    public static final String ORDER_CREATE_ROUTING_KEY = "order.create";
    public static final String ORDER_UPDATE_ROUTING_KEY = "order.update";
    public static final String ORDER_DELETE_ROUTING_KEY = "order.delete";

    public static final String USER_CREATE_ROUTING_KEY = "user.create";
    public static final String USER_UPDATE_ROUTING_KEY = "user.update";
    public static final String USER_DELETE_ROUTING_KEY = "user.delete";

    public static final String ORDER_DLQ = "order.dlq";
    public static final String USER_DLQ = "user.dlq";

    public static final String ORDER_DLX = "order.dlx";
    public static final String USER_DLX = "user.dlx";

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
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(
                rabbitTemplate,
                "",
                ""
        ) {
            protected void send(String exchange, String routingKey, Message message) {
                throw new RuntimeException("Retries exhausted - message will be sent to DLX");
            }
        };
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            RetryTemplate retryTemplate) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setRetryTemplate(retryTemplate);
        factory.setDefaultRequeueRejected(true);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
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
                .withArgument("x-dead-letter-exchange", ORDER_DLX)
                .withArgument("x-dead-letter-routing-key", "order.create.dlq")
                .build();
    }

    @Bean
    public Queue orderUpdateQueue() {
        return QueueBuilder.durable(ORDER_UPDATE_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_DLX)
                .withArgument("x-dead-letter-routing-key", "order.update.dlq")
                .build();
    }

    @Bean
    public Queue orderDeleteQueue() {
        return QueueBuilder.durable(ORDER_DELETE_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_DLX)
                .withArgument("x-dead-letter-routing-key", "order.delete.dlq")
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
                .withArgument("x-dead-letter-exchange", USER_DLX)
                .withArgument("x-dead-letter-routing-key", "user.create.dlq")
                .build();
    }

    @Bean
    public Queue userUpdateQueue() {
        return QueueBuilder.durable(USER_UPDATE_QUEUE)
                .withArgument("x-dead-letter-exchange", USER_DLX)
                .withArgument("x-dead-letter-routing-key", "user.update.dlq")
                .build();
    }

    @Bean
    public Queue userDeleteQueue() {
        return QueueBuilder.durable(USER_DELETE_QUEUE)
                .withArgument("x-dead-letter-exchange", USER_DLX)
                .withArgument("x-dead-letter-routing-key", "user.delete.dlq")
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
}
