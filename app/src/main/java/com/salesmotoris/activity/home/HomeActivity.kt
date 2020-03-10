package com.salesmotoris.activity.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.salesmotoris.R
import com.salesmotoris.activity.splash.SplashActivity
import com.salesmotoris.fragment.ProfileFragment
import com.salesmotoris.fragment.transaction.TransactionFragment
import com.salesmotoris.fragment.home.HomeFragment
import com.salesmotoris.fragment.stock.StockFragment
import com.salesmotoris.fragment.visitation.VisitationFragment
import com.salesmotoris.library.SalesMotorisPref
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.alert


class HomeActivity : AppCompatActivity() {

    private lateinit var navHeader: View
    private lateinit var navHeaderName: TextView
    private lateinit var navHeaderWebsite: TextView
    private lateinit var navHeaderBg: ImageView
    private lateinit var navHeaderProfile: ImageView
    private lateinit var toolbarTitle: Array<String>

    private var navItemIndex = 0

    companion object{
        private const val TAG_HOME = "home"
        private const val TAG_PROFILE = "profile"
        private const val TAG_LOGOUT = "logout"
    }
    private var currentTag = TAG_HOME

    private val mHandler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navHeader = nav_main.getHeaderView(0)
        navHeaderName = navHeader.findViewById(R.id.textview_header_name)
        navHeaderWebsite = navHeader.findViewById(R.id.textview_header_website)
        navHeaderBg = navHeader.findViewById(R.id.imageview_header_bg)
        navHeaderProfile = navHeader.findViewById(R.id.imageview_header_profile)

        toolbarTitle = resources.getStringArray(R.array.nav_item_activity_titles)

        loadNavHeader()

        setUpNavigationView()

        if (savedInstanceState == null) {
            navItemIndex = 0
            currentTag = TAG_HOME
            loadHomeFragment()
        }

        initBottomNav()
    }

    private fun loadNavHeader() {
        navHeaderName.text = SalesMotorisPref(this).username
        navHeaderWebsite.text = SalesMotorisPref(this).email

        Glide.with(this)
            .load("https://api.androidhive.info/images/nav-menu-header-bg.jpg")
            .transition(DrawableTransitionOptions().crossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(navHeaderBg)

        Glide.with(this)
            .load(R.drawable.ic_user)
            .transition(DrawableTransitionOptions().crossFade())
            .thumbnail(0.5f)
            .transform(CircleTransform())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(navHeaderProfile)
    }

    private fun loadHomeFragment() {
        selectNavMenu()

        setToolbarTitle()

        if (supportFragmentManager.findFragmentByTag(currentTag) != null) {
            drawer_layout.closeDrawers()
            return
        }

        val mPendingRunnable = Runnable {
            // update the main content by replacing fragments
            val fragment = getHomeFragment()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.setCustomAnimations(
//                R.anim.anim_fade_in,
//                R.anim.anim_fade_out
//            )
            fragment?.let {
                fragmentTransaction.replace(R.id.frame_main, it, currentTag)
            }
            fragmentTransaction.commitAllowingStateLoss()
        }

        // If mPendingRunnable is not null, then add to the message queue
        mHandler.post(mPendingRunnable)

        drawer_layout.closeDrawers()

        invalidateOptionsMenu()
    }

    private fun getHomeFragment(): Fragment? {
        return when (navItemIndex) {
            0 -> {
                // home
                bottomnav_main.currentItem = 0
                bottomnav_main.visibility = View.VISIBLE
                HomeFragment()
            }
            1 -> {
                // profile
                bottomnav_main.visibility = View.GONE
                ProfileFragment()
            }
            2 -> {
                // logout
                showLogoutDialog()
                null
            }
            else -> HomeFragment()
        }
    }

    private fun showLogoutDialog() {
        val context: Context = this
        alert("Apakah anda yakin ingin menghapus data anda pada perangkat ini?", "Keluar") {
            positiveButton("Keluar"){
                SalesMotorisPref(context).clearData()
                startActivity(Intent(context, SplashActivity::class.java))
                finish()
            }
            negativeButton("Tidak"){ it.dismiss() }
        }.show()
    }

    private fun setToolbarTitle() {
        toolbar_main.title = toolbarTitle[navItemIndex]
    }

    private fun selectNavMenu() {
        nav_main.menu.getItem(navItemIndex).isChecked = true
    }

    private fun setUpNavigationView() {
        nav_main.setNavigationItemSelectedListener { menuItem ->
            // This method will trigger on item Click of navigation menu
            //Check to see which item was being clicked and perform appropriate action
            when (menuItem.itemId) {
                //Replacing the main content with ContentFragment Which is our Inbox View;
                R.id.nav_home -> {
                    navItemIndex = 0
                    currentTag = TAG_HOME
                }
                R.id.nav_profile -> {
                    navItemIndex = 1
                    currentTag = TAG_PROFILE
                }
                R.id.nav_logout -> {
                    navItemIndex = 2
                    currentTag = TAG_LOGOUT
                }
                else -> navItemIndex = 0
            }

            //Checking if the item is in checked state or not, if not make it in checked state
            menuItem.isChecked = !menuItem.isChecked
            menuItem.isChecked = true

            loadHomeFragment()

            true
        }


        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar_main,
            R.string.openDrawer,
            R.string.closeDrawer
        ) {

        }

        //Setting the actionbarToggle to drawer layout
        drawer_layout.addDrawerListener(actionBarDrawerToggle)

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawers()
            return
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (navItemIndex != 0) {
            navItemIndex = 0
            currentTag = TAG_HOME
            loadHomeFragment()
            return
        }

        super.onBackPressed()
    }

    interface RefreshStock {
        fun refresh()
    }
    var refreshStock: RefreshStock? = null
    fun updateStock(listener: RefreshStock) {
        refreshStock = listener
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==  1) {
            if (resultCode == Activity.RESULT_OK) {
            }
        }else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                refreshStock?.refresh()
            }
        }
    }

    private fun initBottomNav() {
        val item1 = AHBottomNavigationItem("Beranda", R.drawable.ic_home)
        val item2 = AHBottomNavigationItem("Kunjungan", R.drawable.ic_note)
        val item3 = AHBottomNavigationItem("Transaksi", R.drawable.ic_transaction)
        val item4 = AHBottomNavigationItem("Stok", R.drawable.ic_box)

        bottomnav_main.addItem(item1)
        bottomnav_main.addItem(item2)
        bottomnav_main.addItem(item3)
        bottomnav_main.addItem(item4)

        bottomnav_main.defaultBackgroundColor = Color.parseColor("#FFFFFF")
        bottomnav_main.accentColor = ContextCompat.getColor(this,
            R.color.colorPrimaryDark
        )
        bottomnav_main.inactiveColor = ContextCompat.getColor(this, R.color.colorDisable)

        bottomnav_main.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        bottomnav_main.currentItem = 0

        bottomnav_main.setOnTabSelectedListener { position, _ ->
            var bottomNavFragment: Fragment? = null
            when (position) {
                0 -> {
                    bottomNavFragment =
                        HomeFragment()
                    toolbar_main.title = "Beranda"
                }
                1 -> {
                    bottomNavFragment =
                        VisitationFragment()
                    toolbar_main.title = "Kunjungan"
                }
                2 -> {
                    bottomNavFragment =
                        TransactionFragment()
                    toolbar_main.title = "Transaksi"
                }
                3 -> {
                    bottomNavFragment =
                        StockFragment()
                    toolbar_main.title = "Stok"
                }
            }
            bottomNavFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_main, it)
                    .commitAllowingStateLoss()
            }
            true
        }
    }
}
