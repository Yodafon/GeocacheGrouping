package com.gg.core.config

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.io.ClassPathResource

import scala.collection.mutable
import scala.jdk.CollectionConverters._

@Configuration
class SparkConfig {

  @Bean
  def propertiesFactoryBean: PropertiesFactoryBean = {
    val propertiesFactoryBean = new PropertiesFactoryBean()
    val resources: ClassPathResource = new ClassPathResource("config/geocachegrouping.properties")
    propertiesFactoryBean.setLocation(resources)
    propertiesFactoryBean.afterPropertiesSet()
    val prop = propertiesFactoryBean.getObject
    propertiesFactoryBean
  }

  @Bean
  def propertySourcesPlaceholderConfigurer(propertiesFactoryBean: PropertiesFactoryBean): PropertySourcesPlaceholderConfigurer = {
    val pspc = new PropertySourcesPlaceholderConfigurer
    pspc setProperties propertiesFactoryBean.getObject
    pspc setIgnoreUnresolvablePlaceholders true
    pspc
  }


  def sparkConfFactoryBean: SparkConf = {

    val set = propertiesFactoryBean.getObject.entrySet()

    var sparkProperties: mutable.Map[String, String] = propertiesFactoryBean.getObject.asScala
    set.stream()
      .forEach(item => sparkProperties = sparkProperties + ((item.getKey.asInstanceOf[String], item.getValue.asInstanceOf[String])))
    val sparkConf: SparkConf = new SparkConf().setAppName("GeocachingGrouping")
      .setAll(sparkProperties)
    sparkConf
  }

  @Bean
  def sparkSession: SparkSession = SparkSession.builder().config(sparkConfFactoryBean).getOrCreate()

  @Bean def streamingContext(sparkSession: SparkSession, @Value("${spark.sql.streaming.checkpointLocation}") checkpointLocation: String = null): StreamingContext = {
    val context = new StreamingContext(sparkSession.sparkContext, Seconds(10))
    context.checkpoint(checkpointLocation)
    context
  }


}
