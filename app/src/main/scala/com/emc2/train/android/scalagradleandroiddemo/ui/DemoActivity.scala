package com.emc2.train.android.scalagradleandroiddemo.ui

import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.emc2.train.android.scalagradleandroiddemo.common.SHttpActivity
import org.scaloid.common._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Demo entry activity
  *
 * Created by IT on 2017/8/8.
 */
class DemoActivity extends AppCompatActivity with SActivity with SHttpActivity {

  private val contents: Array[String] = Array("List activity", "Hello world", "...")

  onCreate {

    contentView = new SVerticalLayout {
      SButton("Button", reqTest(resultText))
      SListView().<<.fill.>>.adapter(new ArrayAdapter[String](ctx, android.R.layout.simple_list_item_1, contents)).onItemClick(
        (_: android.widget.AdapterView[_], _: android.view.View, p3: Int, _: Long) => toast(contents(p3))
      )
      val resultText = STextView().wrap
    }

  }

  def reqTest(text: STextView): Unit = {
    toast("do volley request")

    // text.text = "hello"

    /*
    val queue: RequestQueue = Volley.newRequestQueue(ctx)
    val url: String = "https://www.baidu.com"
    val request: StringRequest = new StringRequest(Request.Method.GET, url, new Listener[String] {
      override def onResponse(response: String): Unit = {
        text.text = s"Response is: ${response.substring(0, 500)}"
      }
    }, new ErrorListener {
      override def onErrorResponse(error: VolleyError): Unit = {
        text.text = s"Error. ${error.getMessage}"
      }
    })
    queue.add(request)
    */

//    implicit val queue: RequestQueue = Volley.newRequestQueue(ctx)
    val url: String = "https://www.baidu.com"
    val future: Future[String] = get[String](url)
    future.map {
      result: String => runOnUiThread {
        text.text = s"Response is: ${result.substring(0, 500)}"
      }
    }


//    future.onComplete {
//      case Success(result) => {
//        runOnUiThread(
//          text.text(s"Response is: ${result.substring(0, 500)}")
//        )
//
//      }
//      case Failure(ex) => {
//        runOnUiThread(
//          text.text(s"Error. ${ex.getMessage}")
//        )
//
//      }
//    }

  }

}
