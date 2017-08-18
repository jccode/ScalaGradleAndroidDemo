package com.emc2.train.android.scalagradleandroiddemo.common

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
    req(Request.Method.GET, url, params, headers)
  }

  def post[T: RequestBuilder](url: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())(implicit rq: RequestQueue): Future[T] = {
    req(Request.Method.POST, url, params, headers)
  }

  def delete[T: RequestBuilder](url: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())(implicit rq: RequestQueue): Future[T] = {
    req(Request.Method.DELETE, url, params, headers)
  }

  def put[T: RequestBuilder](url: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())(implicit rq: RequestQueue): Future[T] = {
    req(Request.Method.PUT, url, params, headers)
  }

  def req[T: RequestBuilder](method: Int, url: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())(implicit rq: RequestQueue): Future[T] = {
    val builder = implicitly[RequestBuilder[T]]
    val result = builder.request(SReq(method, url, params, headers))
    rq.add(result._1)
    result._2
  }

}


case class SReq(method: Int, url: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map())

trait RequestBuilder[T] {
  def request(t: SReq): (Request[T], Future[T])
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
    override def request(t: SReq): (Request[String], Future[String]) = {
      val p = Promise[String]
      val req: Request[String] = new StringRequest(t.method, t.url, (res: String) => success(p, res), (err: VolleyError) => failure(p, err))
      (req, p.future)
    }
  }

  implicit val jsonObjectRequestBuilder: RequestBuilder[JSONObject] = new RequestBuilder[JSONObject] {
    override def request(t: SReq): (Request[JSONObject], Future[JSONObject]) = {
      val p = Promise[JSONObject]
      val param: JSONObject = null
      val req = new JsonObjectRequest(t.method, t.url, param, (res: JSONObject) => success(p, res), (err: VolleyError) => failure(p, err))
      (req, p.future)
    }
  }

  implicit val jsonArrayRequestBuilder: RequestBuilder[JSONArray] = new RequestBuilder[JSONArray] {
    override def request(t: SReq): (Request[JSONArray], Future[JSONArray]) = {
      val p = Promise[JSONArray]
      val param: JSONArray = null
      val req = new JsonArrayRequest(t.method, t.url, param, (res: JSONArray) => success(p, res), (err: VolleyError) => failure(p, err))
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