package com.emc2.train.android.scalagradleandroiddemo.ui

import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import org.scaloid.common._

/**
  * Demo entry activity
  *
 * Created by IT on 2017/8/8.
 */
class DemoEntryActivity extends AppCompatActivity with SActivity {

  private val contents: Array[String] = Array("List activity", "Hello world", "...")

  onCreate {
    val list = new SListView()
    list.adapter = new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, contents)
    list.onItemClick((_: android.widget.AdapterView[_], _: android.view.View, p3: Int, _: Long) => toast(contents(p3)))
    contentView = list
  }

}
