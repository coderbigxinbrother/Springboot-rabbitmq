package com.rabbitmq.api.ack;

import java.util.HashMap;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
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
		String exchange = "test_ack_exchange";
		String routingKey = "ack.save";
		//5 信道发送消息
		for(int i =0; i<5; i ++){
			HashMap<String, Object> header = new HashMap<>();
			header.put("num", i);
			AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
					.deliveryMode(2)
					.contentEncoding("UTF-8")
					.headers(header)
					.build();
			String msg = "Hello RabbitMQ ACK Message" +i;
			channel.basicPublish(exchange, routingKey, true, properties, msg.getBytes());
		}
	}
}
