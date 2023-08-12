package com.example.myproj

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myproj.databinding.ActivityMainBinding
import com.example.myproj.pageDrawerAdapter.PageAdapter
import com.example.myproj.uiHolder.setting.SettingFragment
import com.google.android.material.navigation.NavigationView


private lateinit var binding: ActivityMainBinding
private lateinit var toggle: ActionBarDrawerToggle
private lateinit var drawerLayout : DrawerLayout
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        when (item.itemId) {
            R.id.setting -> {
                binding.navView.visibility = View.GONE
                binding.tabLayout.visibility = View.GONE
                binding.viewPager.visibility = View.GONE
                item.isVisible = false
                supportFragmentManager.beginTransaction()
                    .replace(R.id.drawerL, SettingFragment())
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
    }
}