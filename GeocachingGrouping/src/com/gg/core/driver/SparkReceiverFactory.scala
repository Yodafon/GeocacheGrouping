package com.gg.core.driver

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class SparkReceiverFactory {

  @transient
  @Autowired val applicationContext: ApplicationContext = null

  def getSparReceiver: SparkReceiver = applicationContext.getBean(classOf[SparkReceiver])

}
