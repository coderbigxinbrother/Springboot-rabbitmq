package com.rabbitmq.api.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer {
	public static void main(String[] args) throws Exception {
		//1 创建一个ConnectionFactory, 并进行配置
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.2.150");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		//2 通过连接工厂创建连接
		Connection connection = connectionFactory.newConnection();
		//3 通过connection创建一个Channel
		Channel channel = connection.createChannel();
		//4 声明交换机和队列 然后进行绑定设置, 最后制定路由Key
		String exchangeName = "test_consumer_exchange";
		String routingKey = "consumer.#";
		String queueName = "test_consumer_queue";
		
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
//		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
		//5 创建消费者 
		channel.basicConsume(queueName, true,new MyConsumer(channel));
	}
}
