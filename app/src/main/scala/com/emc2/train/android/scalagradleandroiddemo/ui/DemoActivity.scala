package com.emc2.train.android.scalagradleandroiddemo.ui

import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.ArrayAdapter
import org.scaloid.common.{SEditText, _}

/**
  * Demo entry activity
  *
 * Created by IT on 2017/8/8.
 */
class DemoActivity extends AppCompatActivity with SActivity {

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
    toast("hello")
    text.text = "hello"
  }

}
