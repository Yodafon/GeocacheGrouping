package com.gg.core.driver

import com.gg.generated.Gpx.Wpt
import com.gg.generated.{GeocacheByCounty, GeocacheByRegion, GeocacheDetail}
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

import java.time.LocalDateTime
import javax.xml.datatype.DatatypeFactory


@Component
class GeocacheGroupingDriver(context: StreamingContext, sparkReceiver: SparkReceiver
                             , @transient jmsTemplate: JmsTemplate
                            ) extends Serializable {
  @transient val instance = DatatypeFactory.newInstance


  def createGeocacheByCounty(county: String, region: String, count: Int): GeocacheByCounty = {
    val geocacheByCounty = new GeocacheByCounty
    geocacheByCounty.setCounty(county)
    geocacheByCounty.setRegion(region)
    geocacheByCounty.setCount(count)
    geocacheByCounty.setTimestamp(instance.newXMLGregorianCalendar(LocalDateTime.now.toString))
    geocacheByCounty
  }

  def createGeocacheByRegion(region: String, count: Int): GeocacheByRegion = {
    val geocacheByRegion = new GeocacheByRegion

    geocacheByRegion.setRegion(region)
    geocacheByRegion.setCount(count)
    geocacheByRegion.setTimestamp(instance.newXMLGregorianCalendar(LocalDateTime.now.toString))
    geocacheByRegion
  }

  def createGeocacheDetail(wpt: Wpt): GeocacheDetail = {
    val detail = new GeocacheDetail
    detail.setCounty(wpt.getCache.getCountry)
    detail.setRegion(wpt.getCache.getState)
    detail.setLong(wpt.getLon)
    detail.setLat(wpt.getLat)
    detail.setName(wpt.getCache.getName)
    detail.setType(wpt.getCache.getType)
    detail.setElevation(wpt.getEle)
    detail.setId(wpt.getCache.getId)
    detail
  }


  val updateFunc =
    (newData: Seq[Int], state: Option[Int]) => {
      val currentCount = newData.sum
      val previousCount = state.getOrElse(0)
      Some(currentCount + previousCount)
    }

  val receivedCaches: ReceiverInputDStream[Wpt] = context.receiverStream(sparkReceiver)
  val regionsDStream: DStream[String] = receivedCaches.map(_.getCache.getState)
  val countiesDStream: DStream[(String, String)] = receivedCaches.map(item => (item.getCache.getCountry, item.getCache.getState))

  val cachesByCounties: DStream[((String, String), Int)] = countiesDStream
    .map(county => ((county._1, county._2), 1))
    .reduceByKey((a, b) => a + b)
    .updateStateByKey(updateFunc)


  val cachesByRegion: DStream[(String, Int)] = regionsDStream.map(region => (region, 1))
    .reduceByKey((a, b) => a + b)
    .updateStateByKey(updateFunc)

  cachesByCounties.foreachRDD(rdd => rdd.collect()
    .map(county => createGeocacheByCounty(county._1._1, county._1._2, county._2))
    .foreach(item => jmsTemplate.convertAndSend("DEV.APP.COUNTY", item))
  )

  cachesByRegion.foreachRDD(rdd => rdd.collect()
    .map(region => createGeocacheByRegion(region._1, region._2))
    .foreach(item => jmsTemplate.convertAndSend("DEV.APP.REGION", item))
  )

  receivedCaches.foreachRDD(rdd =>
    rdd.collect()
      .map(wpt => createGeocacheDetail(wpt))
      .foreach(item => jmsTemplate.convertAndSend("DEV.APP.GEOCACHEDETAILS", item))
  )


  context.start()
  context.awaitTermination() // Wait for the computation to terminate
}