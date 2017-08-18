package com.emc2.train.android.scalagradleandroiddemo.ui

import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.emc2.train.android.scalagradleandroiddemo.common.SHttpActivity
import org.json.JSONArray
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

      new SVerticalLayout {
        SButton("Ajax plain text", plainTextReq(resultText))
        SButton("Ajax get json", jsonGetReq(resultText))
        SButton("Ajax post json", toast("post demo"))
      }.fw.here

      SListView().<<.fill.>>.adapter(new ArrayAdapter[String](ctx, android.R.layout.simple_list_item_1, contents)).onItemClick(
        (_: android.widget.AdapterView[_], _: android.view.View, p3: Int, _: Long) => toast(contents(p3))
      )
      val resultText = STextView().wrap
    }

  }

  def plainTextReq(text: STextView): Unit = {
    val url: String = "https://www.baidu.com"
    val future: Future[String] = get[String](url)
    future.map {
      result => runOnUiThread {
        text.text = s"Response is: ${result.substring(0,500)}"
      }
    }
  }

  def jsonGetReq(text: STextView): Unit = {
    val url: String = "http://jsonplaceholder.typicode.com/users"
    val future: Future[JSONArray] = get[JSONArray](url)
    future.map {
      result: JSONArray => runOnUiThread {
        text.text = s"Response is: ${result.join("\n")}"
      }
    }
  }


  def reqTest(text: STextView): Unit = {
    toast("do volley request")

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
