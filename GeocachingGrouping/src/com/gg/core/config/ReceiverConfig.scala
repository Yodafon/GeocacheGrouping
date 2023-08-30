package com.gg.core.config

import com.ibm.mq.jakarta.jms.MQQueueConnectionFactory
import com.ibm.msg.client.jakarta.wmq.compat.base.internal.MQC
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class ReceiverConfig {

  @Bean
  def connectionFactory: MQQueueConnectionFactory = {
    val connectionFactory = new MQQueueConnectionFactory()
    connectionFactory.setHostName("localhost")
    connectionFactory.setPort(1414)
    connectionFactory.setStringProperty(MQC.USER_ID_PROPERTY, "app");
    connectionFactory.setStringProperty(MQC.PASSWORD_PROPERTY, "passw0rd");
    connectionFactory.setChannel("DEV.APP.SVRCONN")
    connectionFactory.setQueueManager("QM1")
    connectionFactory
  }

}
