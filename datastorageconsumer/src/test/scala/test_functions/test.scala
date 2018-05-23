package test_functions

import java.io.File
import java.util
import java.util.Properties

import app.test.RunAppTmp
import com.fasterxml.jackson.databind.{DeserializationContext, KeyDeserializer, ObjectMapper, ObjectWriter}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.govcloud.digst.Organisation
import org.junit.Test

class test {


  @Test
  def test(): Unit = {

    var props = new Properties()
    props.put("sss","dddd")
    var app = new RunAppTmp()
    app.convertToMap(props)
  }

  @Test
  def test_avro_json(): Unit = {


    var obj:ObjectMapper = new ObjectMapper()
    var simp = new SimpleModule()
    simp addKeyDeserializer(classOf[CharSequence],new Custom())
    obj.registerModule(simp)
    val mapping:Organisation = new Organisation()
    mapping.setName("hello man")
    mapping.setId("12")
    mapping.setParentid("11")
    mapping.setTemplate("www.sss.dk")
    mapping.setFoaauthorityid("no way")
    mapping.setExtras(new util.HashMap[CharSequence, CharSequence]())

    val s =mapping.toString

    val res = obj.readValue(s, classOf[Organisation])

//    var w:ObjectWriter = obj.writeValue()
//    var b:Array[Byte]=w.writeValueAsBytes(s)
//
//    obj.writeValue(new File("p.json"), b)
//
//    print(res)

  }


  class Custom extends KeyDeserializer
  {
    override def deserializeKey(s: String, deserializationContext: DeserializationContext): String =
    {

      s
    }
  }



}

