package com.emc2.train.android.scalagradleandroiddemo.service

import org.scaloid.common.LocalService

import scala.util.Random

/**
   * Random number service
   *
  * Created by IT on 2017/8/27.
  */
class RandomNumberService extends LocalService {

  private def generator = new Random()

  def getRandomNumber = generator.nextInt(100)

}
