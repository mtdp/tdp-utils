package com.github.mtdp.util;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ScheduledMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.fastjson.JSON;

/**
 * 
 *
 * @Description mq队列工具
 * @author wangguoqing
 * @date 2016年7月31日上午10:59:04
 *
 */
public class QueueUtil {
	
	/** 延时时间 10ms **/
	public static int DELAY_TIME = 10000;

	/**
	 * 发送格式为json的消息
	 * @param queueName
	 * @param obj
	 */
	public static void send2Queue(JmsTemplate jmsTemplate,String queueName,final Object obj){
		jmsTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(ObjectToJson(obj));
			}
		});
	}
	
	/**
     * 发送延时信息到队列
     * @param queueName 队列名称
     * @param str       所传的字符串
     * @param delayTime 延时时间,例如:delayTime = 2 * 10 * 1000;表示20s
     * 
     * @备注      发送延时消息是,需要在activemq的配置中,配置activemq.xml中修改成如下配置<br/>
     *          <broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}" schedulerSupport="true">
     */
    public static void send2DelayQueue(JmsTemplate jmsTemplate,String queueName,final Object obj,final long delayTime){
       jmsTemplate.send(queueName, new MessageCreator() {
          @Override
          public Message createMessage(Session session) throws JMSException {
              TextMessage tm = session.createTextMessage(ObjectToJson(obj));
              tm.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delayTime); 
              return tm;
          }
      });
    }
	
	public static void send2Queue(JmsTemplate jmsTemplate,final Object obj){
		jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(ObjectToJson(obj));
			}
		});
	}
	
	public static String ObjectToJson(Object obj){
		String result = "";
		result = JSON.toJSONString(obj);
		return result;
	}
	
	public static Object JsonToBean(String json){
		return JSON.parse(json);
	}
}
