package com.gg.core.config

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.jms.support.converter.{MappingJackson2MessageConverter, MessageConverter, MessageType}

@Configuration
class JMSConfig {

  @Bean
  def createMessageConverter(): MessageConverter = {
    val converter = new MappingJackson2MessageConverter
    converter.setTargetType(MessageType.TEXT)
    converter.setTypeIdPropertyName("_type")
    converter
  }


}
