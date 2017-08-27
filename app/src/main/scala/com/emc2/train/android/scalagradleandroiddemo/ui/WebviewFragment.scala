package com.emc2.train.android.scalagradleandroiddemo.ui

import android.os.Bundle
import android.view.{LayoutInflater, View, ViewGroup}
import android.webkit.{WebView, WebViewClient}
import android.widget.{Button, TextView}
import com.emc2.train.android.scalagradleandroiddemo.R
import org.scaloid.common._
import org.scaloid.support.v4.SFragment

/**
  * webview fragment
 * Created by IT on 2017/8/27.
 */
class WebviewFragment extends SFragment {
  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val v: View = inflater.inflate(R.layout.fragment_webview, container, false)
    val webview: WebView = v.find[WebView](R.id.webview)
//    webview.settings.setJavaScriptEnabled(true)
    webview.settings.setUserAgentString("mozilla/5.0 (linux; u; android 4.1.2; zh-cn; mi-one plus build/jzo54k) applewebkit/534.30 (khtml, like gecko) version/4.0 mobile safari/534.30 micromessenger/5.0.1.352")
    webview.setWebViewClient(new WebViewClient() {
      override def onPageFinished(view: WebView, url: String): Unit = {
        webview.loadUrl("javascript:(function(){ document.body.innerHTML = document.body.innerHTML.replace('<video','<abc'); })()")
      }
    })

    val urlText: TextView = v.find[TextView](R.id.urlText)
    v.find[Button](R.id.gotoBtn).onClick(render(webview, urlText))
    v
  }

  def render(webview: WebView, urlText: TextView): Unit = {
    var url = urlText.text.toString
    val reg = "^http://|^https://".r
    if (reg.findFirstIn(url).isEmpty) {
      url = "http://" + url
    }
    webview.loadUrl(url)
  }


}

object WebviewFragment {
  def apply(): WebviewFragment = new WebviewFragment()
}
