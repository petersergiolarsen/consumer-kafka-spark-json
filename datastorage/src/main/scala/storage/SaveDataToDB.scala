package storage

import org.ojai.store.{Connection, DocumentStore, DriverManager}


// todo: https://github.com/mapr-demos/ojai-2-examples/tree/master/src/main/java/com/mapr/ojai/examples
/***
  * tableName = /usr/mapr/tabname
  * @param connectionString
  * @param tableName
  */

class SaveDataToDB[DataObject](connectionString:String, tableName:String) {

  val LOG: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(classOf[SaveDataToDB[DataObject]])
  var connection:Connection = _
  var store:DocumentStore=_

  def connectToDb(): Unit = {

    try
      {

        connection = DriverManager.getConnection(connectionString)

      }
    catch
      {

        case e:Exception => LOG.error(s"Error in ${connectionString}: ", e.printStackTrace())

      }

  }

  def connectToTable(): Unit = {

    try {

      store = connection.getStore(tableName)

    }
    catch
      {

        case e:Exception => LOG.error(s"Error in ${tableName}", e.printStackTrace())

      }


  }

  def saveData(dataObject:DataObject): Unit = {



  }



}
