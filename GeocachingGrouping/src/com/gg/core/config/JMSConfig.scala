package com.gg.core.config

import com.ibm.mq.jakarta.jms.MQQueueConnectionFactory
import com.ibm.msg.client.jakarta.wmq.compat.base.internal.MQC
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.jms.support.converter.{MappingJackson2MessageConverter, MessageConverter, MessageType}

@Configuration
class JMSConfig {

  @Value("${ibm.mq.host}")
  val host: String = null;
  @Value("${ibm.mq.port}")
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
  def connectionFactory: MQQueueConnectionFactory = {
    val connectionFactory = new MQQueueConnectionFactory()
    connectionFactory.setHostName(host)
    connectionFactory.setPort(port)
    connectionFactory.setStringProperty(MQC.USER_ID_PROPERTY, userId);
    connectionFactory.setStringProperty(MQC.PASSWORD_PROPERTY, password);
    connectionFactory.setChannel(channel)
    connectionFactory.setQueueManager(queueManager)
    connectionFactory
  }


}
