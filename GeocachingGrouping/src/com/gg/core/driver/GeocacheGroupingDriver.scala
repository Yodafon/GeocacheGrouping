package com.gg.core.driver

import com.gg.generated.Gpx.Wpt
import com.gg.generated.{GeocacheByCounty, GeocacheByRegion, GeocacheDetail}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{State, StateSpec, StreamingContext, Time}
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

import java.time.LocalDateTime
import javax.xml.datatype.DatatypeFactory


@Component
class GeocacheGroupingDriver(context: StreamingContext, sparkReceiver: SparkReceiver,
                             @transient jmsTemplate: JmsTemplate
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
    detail.setLon(wpt.getLon)
    detail.setLat(wpt.getLat)
    detail.setName(wpt.getCache.getName)
    detail.setType(wpt.getCache.getType)
    detail.setElevation(wpt.getEle)
    detail.setId(wpt.getCache.getId)
    detail
  }


  val regionUpdateFunc =
    StateSpec.function((batchTime: Time, key: String, value: Option[Int], state: State[Int]) => {
      if (state.exists()) {
        state.update(value.get + state.get())
        Some((key, state.get()))
      } else {
        state.update(value.get)
        Some((key, value.get))
      }
    })

  val countyUpdateFunc =
    StateSpec.function((batchTime: Time, key: (String, String), value: Option[Int], state: State[Int]) => {
      if (state.exists()) {
        state.update(value.get + state.get()) //increase same state with same key
        Some((key, state.get())) //return back increased value
      } else {
        state.update(value.get) ///add new value to state with corresponding key
        Some((key, value.get)) //return new value
      }
    })


  val gpxUpdateFunc =
    StateSpec.function((batchTime: Time, key: String, value: Option[Wpt], state: State[Wpt]) => {
      if (state.exists()) { //if actual gpx exists, stop additional processing by returning None
        None
      } else {
        state.update(value.get) //add new value to state with corresponding key
        Some((key, value.get)) //return new value
      }
    })

  val receivedCaches: DStream[(String, Wpt)] =
    context.receiverStream(sparkReceiver)
      .map(wpt => (wpt.getCache.getName, wpt))
      .mapWithState(gpxUpdateFunc)
  // TODO: (key: String, (GPX, doCalc:Boolean)) pair, if existing GPX was updated, shouldn't recalculate additional groupings
  //  but GPX should be published onto MQ


  val countiesDStream: DStream[((String, String), Int)] = receivedCaches
    .map(item => (item._2.getCache.getCountry, item._2.getCache.getState))
    // TODO: filter those elemenets out, which has doCalc=false to ignore from calculations
    .map(county => ((county._1, county._2), 1))
    .mapWithState(countyUpdateFunc)

  countiesDStream.foreachRDD(rdd => rdd.collect()
    .map(county => createGeocacheByCounty(county._1._1, county._1._2, county._2))
    .foreach(item => jmsTemplate.convertAndSend("DEV.APP.COUNTY", item))
  )

  val regionsDStream: DStream[(String, Int)] = {
    countiesDStream.map(_._1._2)
      .map(region => (region, 1))
      .mapWithState(regionUpdateFunc)
  }

  regionsDStream.foreachRDD(rdd => rdd.collect()
    .map(region => createGeocacheByRegion(region._1, region._2))
    .foreach(item => jmsTemplate.convertAndSend("DEV.APP.REGION", item))
  )

  receivedCaches.foreachRDD(rdd =>
    rdd.collect()
      .map(wpt => createGeocacheDetail(wpt._2))
      .foreach(item => jmsTemplate.convertAndSend("DEV.APP.GEOCACHEDETAILS", item))
  )


  context.start()
  context.awaitTermination() // Wait for the computation to terminate
}