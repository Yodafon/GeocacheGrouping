package com.gg.core.config

import com.ibm.mq.jakarta.jms.MQQueueConnectionFactory
import com.ibm.msg.client.jakarta.wmq.common.CommonConstants
import com.ibm.msg.client.jakarta.wmq.compat.base.internal.MQC
import jakarta.jms.ConnectionFactory
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.context.annotation.{Bean, Configuration, Profile}
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.{MappingJackson2MessageConverter, MessageConverter, MessageType}

@Configuration
class JMSConfig {

  @Value("${publisher.ibm.mq.host}")
  val host: String = null;
  @Value("${publisher.ibm.mq.port}")
  val port: Int = 0;
  @Value("${ibm.mq.user}")
  val userId: String = null;
  @Value("${ibm.mq.password}")
  val password: String = null;
  @Value("${ibm.mq.channel}")
  val channel: String = null;
  @Value("${ibm.mq.queueManager}")
  val queueManager: String = null;

  @Bean
  def createMessageConverter(): MessageConverter = {
    val converter = new MappingJackson2MessageConverter
    converter.setTargetType(MessageType.TEXT)
    converter.setTypeIdPropertyName("_type")
    converter
  }


  @Bean
  @Profile(Array("dev"))
  def devConnectionFactory: ConnectionFactory = {
    val connectionFactory: MQQueueConnectionFactory = createDefaultConnFactory
    connectionFactory
  }

  @Bean
  @Profile(Array("prod"))
  def prodConnectionFactory: ConnectionFactory = {
    val connectionFactory: MQQueueConnectionFactory = createDefaultConnFactory
    connectionFactory.setIntProperty(CommonConstants.WMQ_CONNECTION_MODE, CommonConstants.WMQ_CM_CLIENT)
    connectionFactory
  }

  private def createDefaultConnFactory = {
    val connectionFactory = new MQQueueConnectionFactory()
    connectionFactory.setHostName(host)
    connectionFactory.setPort(port)
    connectionFactory.setStringProperty(MQC.USER_ID_PROPERTY, userId);
    connectionFactory.setStringProperty(MQC.PASSWORD_PROPERTY, password);
    connectionFactory.setChannel(channel)
    connectionFactory.setQueueManager(queueManager)
    connectionFactory
  }

  @Bean
  def jmsTemplate(messageConverter: MessageConverter, @Autowired connectionFactory: ConnectionFactory): JmsTemplate = {
    val template = new JmsTemplate(connectionFactory)
    template.setMessageConverter(messageConverter)
    template
  }

}