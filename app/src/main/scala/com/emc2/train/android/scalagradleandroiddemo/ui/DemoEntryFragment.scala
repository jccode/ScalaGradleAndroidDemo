package com.emc2.train.android.scalagradleandroiddemo.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.emc2.train.android.scalagradleandroiddemo.R
import org.scaloid.support.v4.SListFragment

/**
  * Demo entry fragment
  *
 * Created by IT on 2017/8/9.
 */
class DemoEntryFragment extends SListFragment {

  private val contents = Array("demo1", "demo2", "...")

  override def onActivityCreated(savedInstanceState: Bundle): Unit = {
    super.onActivityCreated(savedInstanceState)
    listAdapter = new ArrayAdapter[String](getActivity, android.R.layout.simple_list_item_activated_1, contents)
  }

  override def onResume(): Unit = {
    super.onResume()
    activity.asInstanceOf[AppCompatActivity].getSupportActionBar.setTitle("demo entry")
  }
}