package com.emc2.train.android.scalagradleandroiddemo.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.{LayoutInflater, View, ViewGroup}
import com.emc2.train.android.scalagradleandroiddemo.R
import org.scaloid.support.v4.SFragment

/**
  * Index fragment (default welcome page)
  *
 * Created by IT on 2017/8/10.
 */
class IndexFragment extends SFragment {
  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    inflater.inflate(R.layout.fragment_index, container, false)
  }

  override def onResume(): Unit = {
    super.onResume()
    activity.asInstanceOf[AppCompatActivity].getSupportActionBar.setTitle(R.string.app_name)
  }
}