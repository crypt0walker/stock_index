package com.async.stock.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
    /**
     * 重新定义消息序列化的方式，改为基于json格式序列化和反序列化
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 国内大盘信息队列
     * @return
     */
    @Bean
    public Queue innerMarketQueue(){
        return new Queue("innerMarketQueue",true);
    }

    /**
     * 定义路由股票信息的交换机
     * @return
     */
    @Bean
    public TopicExchange innerMarketTopicExchange(){
        return new TopicExchange("stockExchange",true,false);
    }

    /**
     * 绑定队列到指定交换机
     * @return
     */
    @Bean
    public Binding bindingInnerMarketExchange(){
        return BindingBuilder.bind(innerMarketQueue()).to(innerMarketTopicExchange())
                .with("inner.market");
    }
}