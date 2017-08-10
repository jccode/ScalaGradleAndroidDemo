package com.emc2.train.android.scalagradleandroiddemo.ui

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.{AdapterView, ArrayAdapter}
import com.emc2.train.android.scalagradleandroiddemo.R
import org.scaloid.common._

/**
  * Demo entry activity
  *
 * Created by IT on 2017/8/8.
 */
class DemoEntryActivity extends ListActivity with SActivity with OnItemSelectedListener {

  private val contents: Array[String] = Array("List activity", "Hello world", "...")

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    getListView.setOnItemSelectedListener(this)
    setListAdapter(new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, contents))
  }

  override def onItemSelected(adapterView: AdapterView[_], view: View, i: Int, l: Long): Unit = {
    toast(s"Hello ${i}")
    info(s"Item selected ${i}")
  }

  override def onNothingSelected(adapterView: AdapterView[_]): Unit = {}
}
