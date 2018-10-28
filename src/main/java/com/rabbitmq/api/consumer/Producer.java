package com.rabbitmq.api.consumer;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.client.AMQP.BasicProperties;

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
		String exchange = "test_consumer_exchange";
		String routingKey = "consumer.save";
		String routingKeyError = "abc.save";

		String msg = "Hello RabbitMQ Return Message";

		channel.addReturnListener(new ReturnListener() {
			@Override
			public void handleReturn(int replyCode, String replyText, String exchange, String routingKey,
					AMQP.BasicProperties properties, byte[] body) throws IOException {

				System.err.println("---------handle  return----------");
				System.err.println("replyCode: " + replyCode);
				System.err.println("replyText: " + replyText);
				System.err.println("exchange: " + exchange);
				System.err.println("routingKey: " + routingKey);
				System.err.println("properties: " + properties);
				System.err.println("body: " + new String(body));
			}
		});
		//5通过Channel发送数据
		for (int i = 0; i < 5; i++) {
			channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());
		}
		// channel.basicPublish(exchange, routingKeyError, true, null,
		// msg.getBytes());
	}
}
