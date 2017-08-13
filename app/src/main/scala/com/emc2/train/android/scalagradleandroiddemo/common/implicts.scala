package com.emc2.train.android.scalagradleandroiddemo.common

import java.util.concurrent.{LinkedBlockingDeque, ThreadPoolExecutor, TimeUnit}

import android.os.{AsyncTask, Build}

import scala.concurrent.ExecutionContext


object ExecutionContextImplics {

  // ref: http://blog.scaloid.org/2013/11/using-scalaconcurrentfuture-in-android.html
  implicit val exec = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    ExecutionContext.fromExecutor(
      new ThreadPoolExecutor(100, 100, 1000, TimeUnit.SECONDS, new LinkedBlockingDeque[Runnable])
    )
  } else {
    ExecutionContext.fromExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
  }

}