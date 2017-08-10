package com.emc2.train.android.scalagradleandroiddemo

import android.os.Bundle
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener
import android.support.design.widget.{FloatingActionButton, NavigationView, Snackbar}
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.{ActionBarDrawerToggle, AppCompatActivity}
import android.support.v7.widget.Toolbar
import android.view.{Menu, MenuItem, View}
import com.emc2.train.android.scalagradleandroiddemo.ui.{DemoEntryActivity, DemoEntryFragment, IndexFragment}
import org.scaloid.common._

/**
  * Main Activity
  *
  * Created by IT on 2017/8/7.
  */
class MainActivity2 extends AppCompatActivity with SActivity with OnNavigationItemSelectedListener {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val toolbar: Toolbar = find[Toolbar](R.id.toolbar)
    setSupportActionBar(toolbar)

    find[FloatingActionButton](R.id.fab).onClick((view: View) => {
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
    })

    val drawer: DrawerLayout = find[DrawerLayout](R.id.drawer_layout)
    val toggle: ActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer.addDrawerListener(toggle)
    toggle.syncState()

    find[NavigationView](R.id.nav_view).setNavigationItemSelectedListener(this)

    if (savedInstanceState == null) {
      navigateToFragment(getTitle, new IndexFragment)
    }
  }

  override def onBackPressed(): Unit = {
    val drawer = find[DrawerLayout](R.id.drawer_layout)
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.main, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.action_settings => return true
    }
    super.onOptionsItemSelected(item)
  }

  override def onNavigationItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.nav_camera =>
      case R.id.nav_gallery =>
      case R.id.nav_slideshow => navigateToFragment("slideshow", new DemoEntryFragment)
      case R.id.nav_manage => startActivity[DemoEntryActivity]
      case R.id.nav_share =>
      case R.id.nav_send =>
    }

    val drawer: DrawerLayout = find[DrawerLayout](R.id.drawer_layout)
    drawer.closeDrawer(GravityCompat.START)
    true
  }

  private def navigateToFragment(title: CharSequence, fragment: Fragment): Unit = {
//    setTitle(title)
    getSupportFragmentManager.beginTransaction.replace(R.id.main_content, fragment).addToBackStack(null).commit
  }

  override def setTitle(title: CharSequence): Unit = getSupportActionBar.setTitle(title)
}
