package com.gg.core.config

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.io.ClassPathResource

import java.nio.file.Paths
import scala.collection.JavaConverters._
import scala.collection.mutable

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
    System setProperty("hadoop.home.dir", Paths.get("C:\\Users\\Laci\\Desktop\\spark-3.2.1-bin-hadoop3.2").toAbsolutePath.toString)
    pspc
  }


  def sparkConfFactoryBean: SparkConf = {

    val set = propertiesFactoryBean.getObject.entrySet()

    var sparkProperties: mutable.Map[String, String] = propertiesFactoryBean.getObject.asScala
    set.stream()
      .forEach(item => sparkProperties = sparkProperties + ((item.getKey.asInstanceOf[String], item.getValue.asInstanceOf[String])))
    val sparkConf: SparkConf = new SparkConf().setAll(sparkProperties)
    sparkConf
  }

  @Bean
  def sparkSession: SparkSession = SparkSession.builder().config(sparkConfFactoryBean).getOrCreate()


}
