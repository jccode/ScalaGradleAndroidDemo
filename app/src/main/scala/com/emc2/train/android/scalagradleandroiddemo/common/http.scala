package com.emc2.train.android.scalagradleandroiddemo.common

import java.net.URLEncoder

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.support.v4.util.LruCache
import com.android.volley.Response.{ErrorListener, Listener}
import com.android.volley._
import com.android.volley.toolbox.ImageLoader.ImageCache
import com.android.volley.toolbox._
import org.json.{JSONArray, JSONObject}

import scala.concurrent.{Future, Promise}


 /**
  * HTTP Operations
  */
trait SHttpOpts {

  def get[T: RequestBuilder](url: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())(implicit rq: RequestQueue): Future[T] = {
    req(Request.Method.GET, buildUrl(url, params), None, headers)
  }

  def delete[T: RequestBuilder](url: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())(implicit rq: RequestQueue): Future[T] = {
    req(Request.Method.DELETE, buildUrl(url, params), None, headers)
  }

  def post[T: RequestBuilder](url: String, params: Option[T] = None, headers: Map[String, String] = Map())(implicit rq: RequestQueue): Future[T] = {
    req(Request.Method.POST, url, params, headers)
  }

  def put[T: RequestBuilder](url: String, params: Option[T] = None, headers: Map[String, String] = Map())(implicit rq: RequestQueue): Future[T] = {
    req(Request.Method.PUT, url, params, headers)
  }

  def req[T: RequestBuilder](method: Int, url: String, params: Option[T] = None, headers: Map[String, String] = Map())(implicit rq: RequestQueue): Future[T] = {
    val builder = implicitly[RequestBuilder[T]]
    val result = builder.request(SReq(method, url, params, headers))
    rq.add(result._1)
    result._2
  }

  private def encode(s: String): String = URLEncoder.encode(s, "UTF-8")

  private def buildUrl(url: String, params: Map[String, String]): String = {
    if (params.isEmpty) url
    else {
      val queryString = params.toList.map{case (k,v) => s"$k=${encode(v)}"}.mkString("&")
      s"$url?$queryString"
    }
  }
}


case class SReq[T](method: Int, url: String, params: Option[T], headers: Map[String, String] = Map())

trait RequestBuilder[T] {
  def request(t: SReq[T]): (Request[T], Future[T])
}

object RequestBuilder {

  import VolleyImplics._

  def success[T](p: Promise[T], t: T): Unit = {
    p success t
  }

  def failure[T](p: Promise[T], e: VolleyError): Unit = {
    p failure e
  }

  implicit object StringRequestBuilder extends RequestBuilder[String] {
    override def request(t: SReq[String]): (Request[String], Future[String]) = {
      val p = Promise[String]
      val req: Request[String] = new StringRequest(t.method, t.url, (res: String) => success(p, res), (err: VolleyError) => failure(p, err))
      (req, p.future)
    }
  }

  implicit val jsonObjectRequestBuilder: RequestBuilder[JSONObject] = new RequestBuilder[JSONObject] {
    override def request(t: SReq[JSONObject]): (Request[JSONObject], Future[JSONObject]) = {
      val p = Promise[JSONObject]
      val req = new JsonObjectRequest(t.method, t.url, t.params.orNull, (res: JSONObject) => success(p, res), (err: VolleyError) => failure(p, err))
      (req, p.future)
    }
  }

  implicit val jsonArrayRequestBuilder: RequestBuilder[JSONArray] = new RequestBuilder[JSONArray] {
    override def request(t: SReq[JSONArray]): (Request[JSONArray], Future[JSONArray]) = {
      val p = Promise[JSONArray]
      val req = new JsonArrayRequest(t.method, t.url, t.params.orNull, (res: JSONArray) => success(p, res), (err: VolleyError) => failure(p, err))
      (req, p.future)
    }
  }
}


trait VolleyImplics {

  implicit def fun2Listener[T](fun: T => Unit): Listener[T] = {
    new Listener[T] {
      override def onResponse(response: T): Unit = fun(response)
    }
  }

  implicit def fun2ErrorListener(fun: VolleyError => Unit): ErrorListener = {
    new ErrorListener {
      override def onErrorResponse(error: VolleyError): Unit = fun(error)
    }
  }

}

object VolleyImplics extends VolleyImplics



class SVolley(val context: Context,
              private var queue: RequestQueue = null,
              private var loader: ImageLoader = null) {

  def requestQueue(): RequestQueue = {
    if (queue == null) {
      queue = Volley.newRequestQueue(context)
    }
    queue
  }

  def imageLoader(): ImageLoader = {
    if (imageLoader == null) {
      loader = new ImageLoader(requestQueue(), new ImageCache {
        private val cache = new LruCache[String, Bitmap](20)
        override def putBitmap(url: String, bitmap: Bitmap): Unit = cache.put(url, bitmap)
        override def getBitmap(url: String): Bitmap = cache.get(url)
      })
    }
    loader
  }

}


/**
  * Volley singleton. <br/>
  *
  * To obtain an RequestQueue
  * <code>
  *   SVolley(applicationContext).requestQueue
  * </code>
  */
object SVolley {

  private var sVolley: SVolley = null

  def apply(context: Context) = {
    if (sVolley == null) {
      sVolley = new SVolley(context)
    }
    sVolley
  }

}

trait SHttpActivity extends SHttpOpts {
  this: Activity =>
  implicit lazy val requestQueue: RequestQueue = SVolley(this.getApplicationContext).requestQueue
}

trait SHttpFragment extends SHttpOpts {
  this: Fragment =>
  implicit lazy val requestQueue: RequestQueue = SVolley(this.getActivity.getApplicationContext).requestQueue
}