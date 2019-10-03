package com.booboot.vndbandroid.ui.home

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.booboot.vndbandroid.R
import com.booboot.vndbandroid.extensions.Track
import com.booboot.vndbandroid.extensions.currentFragment
import com.booboot.vndbandroid.extensions.isOpen
import com.booboot.vndbandroid.extensions.isPointInsideBounds
import com.booboot.vndbandroid.extensions.isTopLevel
import com.booboot.vndbandroid.extensions.observeNonNull
import com.booboot.vndbandroid.extensions.observeOnce
import com.booboot.vndbandroid.extensions.removeFocus
import com.booboot.vndbandroid.extensions.setLightStatusAndNavigation
import com.booboot.vndbandroid.extensions.toggleBottomSheet
import com.booboot.vndbandroid.model.vndb.AccountItems
import com.booboot.vndbandroid.model.vndbandroid.NOT_SET
import com.booboot.vndbandroid.model.vndbandroid.Preferences
import com.booboot.vndbandroid.ui.base.BaseActivity
import com.booboot.vndbandroid.ui.hometabs.HomeTabsFragment
import com.booboot.vndbandroid.ui.login.LoginActivity
import com.booboot.vndbandroid.ui.vndetails.VNDetailsFragment
import kotlinx.android.synthetic.main.filter_bar_bottom_sheet.*
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.sort_bottom_sheet.*
import kotlinx.android.synthetic.main.vn_details_bottom_sheet.*
import kotlin.math.roundToInt

class HomeActivity : BaseActivity(), View.OnClickListener {
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        setLightStatusAndNavigation()

        if (!Preferences.loggedIn || Preferences.gdprCrashlytics == NOT_SET) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            navigationView.setupWithNavController(findNavController(R.id.navHost))

            var shouldSync = true
            intent.extras?.apply {
                shouldSync = getBoolean(SHOULD_SYNC, true)
                remove(SHOULD_SYNC)
            }

            viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
            viewModel.loadingData.observeNonNull(this, ::showLoading)
            viewModel.accountData.observeNonNull(this, ::showAccount)
            viewModel.errorData.observeOnce(this, ::showError)
            viewModel.getVns()

            if (supportFragmentManager.primaryNavigationFragment == null) {
                if (shouldSync) viewModel.startupSync()
            }
        }
    }

    fun startupSync() = viewModel.startupSync()

    private fun showAccount(accountItems: AccountItems?) {
        accountItems ?: return

        Track.tag(accountItems)
        setMenuCounter(R.id.vnlistFragment, accountItems.vnlist.size)
        setMenuCounter(R.id.votelistFragment, accountItems.wishlist.size)
        setMenuCounter(R.id.wishlistFragment, accountItems.votelist.size)
    }

    // TODO make this work with setupWithNavController without opening a Fragment
    private fun logout(): Boolean {
        // TODO clear all
        viewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu) = true

    override fun onClick(v: View?) = when (v?.id) {
        R.id.floatingSearchButton -> {
//            startActivity(Intent(this, VNSearchActivity::class.java))
        }
        else -> {
        }
    }

    private fun setMenuCounter(itemId: Int, count: Int) {
        navigationView?.let {
            val view = it.menu.findItem(itemId).actionView as TextView
            view.text = if (count > 0) count.toString() else null
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (val fragment = currentFragment()) {
            is HomeTabsFragment -> {
                val touchPoint = Point(ev.rawX.roundToInt(), ev.rawY.roundToInt())
                if (fragment.filterBarBehavior.isOpen() && fragment.filterBar?.isPointInsideBounds(touchPoint) == false) {
                    fragment.filterBar?.removeFocus()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(findNavController(R.id.navHost), drawer)

    override fun onBackPressed() {
        if (drawer?.isDrawerOpen(GravityCompat.START) == true)
            return drawer.closeDrawer(GravityCompat.START)

        when (val fragment = currentFragment()) {
            is HomeTabsFragment -> if (fragment.sortBottomSheetBehavior.isOpen()) {
                fragment.sortBottomSheet?.toggleBottomSheet()
                return
            } else if (fragment.filterBarBehavior.isOpen()) {
                fragment.filterBarBottomSheet?.toggleBottomSheet()
                return
            }
            is VNDetailsFragment -> if (fragment.bottomSheetBehavior.isOpen()) {
                fragment.bottomSheet?.toggleBottomSheet()
                return
            }
        }

        if (isTopLevel()) {
            /* Going back to home screen (don't use super.onBackPressed() because it would kill the app) */
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        } else super.onBackPressed()
    }

    companion object {
        const val SHOULD_SYNC = "SHOULD_SYNC"
    }
}