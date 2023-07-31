package com.example.myproj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myproj.databinding.ActivityMainBinding
import com.example.myproj.uI.PageAdapter
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
            tabLayout.setupWithViewPager(binding.viewPager)
            tabLayout
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
            title = title
            true
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}