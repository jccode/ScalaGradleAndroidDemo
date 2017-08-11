package com.emc2.train.android.scalagradleandroiddemo.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.{AdapterView, ArrayAdapter}
import com.emc2.train.android.scalagradleandroiddemo.R
import org.scaloid.support.v4.SListFragment

/**
  * Demo entry fragment
  *
 * Created by IT on 2017/8/9.
 */
class DemoEntryFragment extends SListFragment with OnItemClickListener {

  private val contents = Array("demo1", "demo2", "...")

  override def onActivityCreated(savedInstanceState: Bundle): Unit = {
    super.onActivityCreated(savedInstanceState)
    listAdapter = new ArrayAdapter[String](getActivity, android.R.layout.simple_list_item_activated_1, contents)
    listView.setOnItemClickListener(this)
  }

  override def onResume(): Unit = {
    super.onResume()
    activity.asInstanceOf[AppCompatActivity].getSupportActionBar.setTitle("demo entry")
  }

  override def onItemClick(adapterView: AdapterView[_], view: View, i: Int, l: Long): Unit = {
    val fragment = i match {
      case 0 => PosterFragment.newInstance(1)
      case _ => PosterFragment.newInstance(2)
    }
    inTransaction {
      transition => {
        transition.replace(R.id.main_content, fragment)
        transition.addToBackStack(null)
      }
    }
  }
}