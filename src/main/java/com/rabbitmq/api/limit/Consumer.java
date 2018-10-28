package com.rabbitmq.api.limit;

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
		String exchangeName = "test_qos_exchange";
		String queueName = "test_qos_queue";
		String routingKey = "qos.#";
		
		channel.exchangeDeclare(exchangeName, "topic", true, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		//消费端的消费者每次最多可消费多少条数据
		channel.basicQos(0, 2, false);
		// 限流方式  第一件事就是 autoAck设置为 false
		channel.basicConsume(queueName, false, new MyConsumer(channel));
	}
}
