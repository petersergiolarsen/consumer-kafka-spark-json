package app.kafka

import java.util
import java.util.Properties

import avro.AvroSerializer
import org.apache.avro.Schema
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}

class ConsumeData[DataObject](propertiesConfigConsumer:Properties,propertiesConfigTopics:Properties){

  val topic: String = propertiesConfigTopics.getProperty("topic.name")
  val pollTopic:Int = propertiesConfigTopics.getProperty("poll.topic").toInt
  val consumer:KafkaConsumer[String, Array[Byte]] = new KafkaConsumer[String, Array[Byte]](propertiesConfigConsumer)

  val LOG: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(classOf[ConsumeData[DataObject]])

  val topics:java.util.List[String] = new util.ArrayList[String]()
  topics.add(topic)
  consumer.subscribe(topics)

  var count:Int = 0

  def setupSerializer(schema:Schema): Unit = {

    AvroSerializer.instance().setUp(schema)
  }

  def run(): Unit = {


    try
    {

      while (true) {

        val records: ConsumerRecords[String, Array[Byte]] = consumer.poll(pollTopic)

        var itr = records.iterator()

        while (itr.hasNext)
          {
            val kv:ConsumerRecord[String, Array[Byte]] =  itr.next()
            val data:DataObject = AvroSerializer.instance().deserialize(kv.value()).asInstanceOf[DataObject]
            println(data.toString)

          }

      }

    }catch
      {
        case e:Exception => LOG.error(s"Error in ConsumeData: ", e.printStackTrace())
      }
    finally {

      println("Messages Sent: ", count)
      count+=1
    }


  }

  def run_temp(): Unit = {


  }


}
