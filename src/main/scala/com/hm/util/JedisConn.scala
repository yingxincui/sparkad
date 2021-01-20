package com.hm.util

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.{Jedis, JedisPool}

object JedisConn {
  def getJedis: Jedis = {
    val config = new GenericObjectPoolConfig()
    val pool = new JedisPool(config, "192.168.10.101", 6379)
    val jedis: Jedis = pool.getResource
    jedis
  }
}
