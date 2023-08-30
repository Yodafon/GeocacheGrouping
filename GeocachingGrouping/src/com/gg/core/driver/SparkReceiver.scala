package com.gg.core.driver

import com.gg.generated.Gpx.Wpt
import com.ibm.mq.jakarta.jms.MQQueueConnectionFactory
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.{MappingJackson2MessageConverter, MessageConverter, MessageType}
import org.springframework.stereotype.Component


@Component
class SparkReceiver extends Receiver[Wpt](StorageLevel.MEMORY_ONLY) {
  val LOGGER: Logger = LoggerFactory.getLogger(classOf[SparkReceiver]);

  @Autowired val connectionFactory: MQQueueConnectionFactory = null;

  var thread: Thread = null;


  override def onStart(): Unit = {
    thread = createThread()
    thread.start()

  }

  private def createThread(): Thread = {
    new Thread() {
      val jmsTemplate: JmsTemplate = new JmsTemplate(connectionFactory)
      jmsTemplate.setMessageConverter(createMessageConverter())
      while (true) {
        var obj: Wpt = jmsTemplate.receiveAndConvert("DEV.APP.LOADER").asInstanceOf[Wpt];
        if (obj != null) {
          store(obj)
        }
      }
    }
  }

  def createMessageConverter(): MessageConverter = {
    val converter = new MappingJackson2MessageConverter
    converter.setTargetType(MessageType.TEXT)
    converter.setTypeIdPropertyName("_type")
    converter
  }

  override def onStop(): Unit = {
    thread.interrupt()
  }
}