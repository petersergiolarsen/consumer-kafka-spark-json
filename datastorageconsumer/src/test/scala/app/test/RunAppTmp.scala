package app.test

import java.io.{FileInputStream, InputStream}
import java.util.Properties

import avro.AvroSerializer
import com.govcloud.digst.Organisation
import org.apache.avro.Schema
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql._
import com.mapr.db._
import com.mapr.db.spark._
import com.mapr.db.spark.impl._
import com.mapr.db.spark.streaming._
import com.mapr.db.spark.sql._
import com.fasterxml.jackson.databind._


case class MapToPOJO(id:String, name:String, parentId:String, template:String) extends Serializable


class RunAppTmp {

  var consumerConfig:String = null
  var topicConfig:String = null
  var sparkConfig:String = null

  def run(propsPath:String): Unit = {


    try {


      setup(propsPath)
      var consumerProp:Properties = readProperties(consumerConfig)

      var topicsProp:Properties = readProperties(topicConfig)
      val topics = topicsProp.getProperty("topic.name")
      val tableName = consumerProp.getProperty("mapr.db.table.name")
      val ssc:StreamingContext = getStreamingContext(getSparkConf())
      val consumerStrategy = ConsumerStrategies.Subscribe[String, Array[Byte]](getTopics(topics),convertToMap(consumerProp))
      val messageDStream = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, consumerStrategy)
      val valueDStream:DStream[MapToPOJO] = messageDStream.map(x=> {

        val org = AvroSerializer.instance().deserialize(x.value()).asInstanceOf[Organisation]

        MapToPOJO(org.getId.toString,
          org.getName.toString,
          org.getParentid.toString,
          org.getTemplate.toString)

      })

      valueDStream.saveToMapRDB(tableName, createTable = false, bulkInsert = true, idFieldPath = "id")

      ssc.start()
      ssc.awaitTermination()
      ssc.stop(stopSparkContext = true, stopGracefully = true)

    }catch {

      case e:Exception => -1

    }



  }

  def getTopics(topics:String): java.util.List[String] = {

    val topicList = new java.util.ArrayList[String]()
    topics.split(",").foreach(x=> {topicList.add(x)})
    topicList
  }

  def setup(pathConf:String): Unit = {

    var props:Properties =readProperties(pathConf)
    consumerConfig = props.getProperty("consumer.config")
    topicConfig = props.getProperty("topic.config")
//    sparkConfig = props.getProperty("spark.config")
  }

  def readProperties(path:String): Properties = {

    var props:Properties = new Properties()
    var ins:InputStream = new FileInputStream(path)
    props.load(ins)
    props
  }

  def getSparkConf(): SparkConf = {

    var sparkConf = new SparkConf()
    sparkConf.setAppName("Digst-borgerdk")
    sparkConf.setMaster("local[2]")

  }

  def getStreamingContext(sparkConf: SparkConf): StreamingContext = {

    val scc = new StreamingContext(sparkConf, Seconds(2))
    scc.sparkContext.setLogLevel("ERROR")
    scc
  }

  def convertToMap(props:Properties): java.util.Map[String,Object] =
  {
    var map:java.util.Map[String, Object] = new java.util.HashMap[String, Object]()
    var names:java.util.Enumeration[String] = props.propertyNames().asInstanceOf[java.util.Enumeration[String]]

    while (names.hasMoreElements)
      {
        val key = names.nextElement()
        val value = props.getProperty(key)
        map.put(key, value)
      }

    map
  }


}


object RunAppTmp
{

  def main(args: Array[String]): Unit = {

    if (args.length<1)
      {
        System.exit(-1)
      }

    var propsPath = args(0)
    var app:RunAppTmp = new RunAppTmp()
    setupAvroSerializer(Organisation.SCHEMA$)
    app.run(propsPath)

  }

  def setupAvroSerializer(schema:Schema): Unit = {

    AvroSerializer.instance().setUp(schema)
  }


}

