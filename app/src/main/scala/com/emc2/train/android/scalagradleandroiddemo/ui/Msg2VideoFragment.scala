package com.emc2.train.android.scalagradleandroiddemo.ui

import android.os.Bundle
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.{Button, EditText}
import com.emc2.train.android.scalagradleandroiddemo.R
import com.emc2.train.android.scalagradleandroiddemo.common.SHttpFragment
import org.scaloid.support.v4.SFragment
import org.scaloid.common._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Msg 2 video fragment
  *
 * Created by IT on 2017/8/27.
 */
class Msg2VideoFragment extends SFragment with SHttpFragment {

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val v: View = inflater.inflate(R.layout.fragment_msg2video, container, false)
    val text: EditText = v.findViewById[EditText](R.id.msgText)
    v.find[Button](R.id.parseBtn).onClick(parse(text))
    v.find[Button](R.id.clearBtn).onClick(text.getText.clear())
    v
  }

  def parse(text: EditText): Unit = {
    val content: String = text.getText.toString
    val url = """(http://t.cn/\w+)""".r
    val urls: List[String] = url.findAllIn(content).toList
    toast(urls.mkString("\r\n"))
    // findVideoUrl("http://t.cn/RC1XvsD")
  }

  def findVideoUrl(url: String): Unit ={
    val headers = Map("User-agent" -> "mozilla/5.0 (linux; u; android 4.1.2; zh-cn; mi-one plus build/jzo54k) applewebkit/534.30 (khtml, like gecko) version/4.0 mobile safari/534.30 micromessenger/5.0.1.352")
    val result: Future[String] = get[String](url, headers = headers)
    result.map(s => toast(s))
  }
}

object Msg2VideoFragment {
  def apply(): Msg2VideoFragment = new Msg2VideoFragment()
}

