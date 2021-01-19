package com.hm.util

object LoggerKIiller {
def killer(): Unit ={
  import org.apache.log4j.{Level, Logger}
  Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
  Logger.getLogger("org.spark_project.jetty.server").setLevel(Level.ERROR)
  Logger.getLogger("org.apache.hadoop.hive.metastore").setLevel(Level.ERROR)
}
}
