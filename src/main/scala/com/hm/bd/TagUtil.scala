package com.hm.bd

import org.apache.spark.sql.Row

object TagUtil {
  def getUserID(row: Row): String = {
    row match {
      case t if(t.getAs("imei")!="")=>"IM: "+t.getAs("imei")
      case t if(t.getAs("mac")!="")=>"IM: "+t.getAs("mac")
      case t if(t.getAs("idfa")!="")=>"IM: "+t.getAs("idfa")
      case t if(t.getAs("openudid")!="")=>"IM: "+t.getAs("openudid")
      case t if(t.getAs("androidid")!="")=>"IM: "+t.getAs("androidid")
    }
  }


}
