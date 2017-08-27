package com.emc2.train.android.scalagradleandroiddemo.ui

import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.emc2.train.android.scalagradleandroiddemo.common.SHttpActivity
import com.emc2.train.android.scalagradleandroiddemo.service.RandomNumberService
import org.json.{JSONArray, JSONObject}
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
  private val random = new LocalServiceConnection[RandomNumberService]()

  onCreate {

    contentView = new SVerticalLayout {

      new SVerticalLayout {
        SButton("Ajax plain text", plainTextReq(resultText))
        SButton("Ajax get json", jsonGetReq(resultText))
        SButton("Ajax post json", jsonPostReq(resultText))
        SButton("Get random number", random(s => toast("number: " + s.getRandomNumber)))
      }.fw.here

      SListView().<<.fill.>>.adapter(new ArrayAdapter[String](ctx, android.R.layout.simple_list_item_1, contents)).onItemClick(
        (_: android.widget.AdapterView[_], _: android.view.View, p3: Int, _: Long) => toast(contents(p3))
      )

      val resultText = STextView()
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
    val url: String = "http://jsonplaceholder.typicode.com/posts/"
    val future: Future[JSONArray] = get[JSONArray](url)
    future.map {
      result: JSONArray => runOnUiThread {
        text.text = result.join("\n")
      }
    }
  }

  def jsonPostReq(text: STextView): Unit = {
    val url: String = "http://10.0.0.66:3000/posts/"
    val param: JSONObject = new JSONObject("{\"title\": \"foo\", \"author\": \"bar\"}")
    val future: Future[JSONObject] = post(url, Some(param))
    future.map {
      result: JSONObject => runOnUiThread {
        text.text = result.toString(4)
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
