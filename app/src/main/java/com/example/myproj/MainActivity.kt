package com.example.myproj

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.example.myproj.dataStore.dataStore
import com.example.myproj.databinding.ActivityMainBinding
import com.example.myproj.pageDrawerAdapter.PageAdapter
import com.example.myproj.uiHolder.Injection
import com.example.myproj.uiHolder.setting.SettingFragment
import com.example.myproj.uiHolder.setting.SettingViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.popBackStack()
        viewModel = ViewModelProvider(
            this, Injection.provideSettingViewModelFactory(
                this.dataStore,
                context = this,
                owner = this
            )
        ).get(SettingViewModel::class.java)
        lifecycle.coroutineScope.launch {
            viewModel.readFromDataStoreColorTheme.collect() {
                Log.d("MainActivity", "onCreate: $it")
                when (it) {
                    "white" -> setTheme(R.style.Theme_MyProj)
                    "skyBlue" -> setTheme(R.style.Theme_MyProjSkyBlue)
                    "darkBlue" -> setTheme(R.style.Theme_MyProjDarkBlue)
                    "violet" -> setTheme(R.style.Theme_MyProjViolet)
                    "lightGreen" -> setTheme(R.style.Theme_MyProjLightGreen)
                    "green" -> setTheme(R.style.Theme_MyProjGreen)
                }
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            viewPager.adapter = PageAdapter(supportFragmentManager)
            tabLayout.setupWithViewPager(binding.viewPager, true)
            viewPager.offscreenPageLimit = 5
        }
        drawerLayout = binding.drawerL
        toggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView(binding.navView)
    }

    private fun navView(navView: NavigationView) {
        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            when (it.itemId) {
                R.id.home -> binding.viewPager.currentItem = 0
                R.id.world -> binding.viewPager.currentItem = 1
                R.id.science -> binding.viewPager.currentItem = 2
                R.id.sport -> binding.viewPager.currentItem = 3
                R.id.environment -> binding.viewPager.currentItem = 4
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.setting -> {
                binding.navView.visibility = View.GONE
                binding.tabLayout.visibility = View.GONE
                binding.viewPager.visibility = View.GONE
                drawerLayout.visibility = View.GONE
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                item.isVisible = false
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameL, SettingFragment())
                    .addToBackStack("setting")
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        binding.navView.visibility = View.VISIBLE
        binding.tabLayout.visibility = View.VISIBLE
        binding.viewPager.visibility = View.VISIBLE
        binding.drawerL.visibility = View.VISIBLE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



}