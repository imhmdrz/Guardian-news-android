package com.example.myproj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myproj.databinding.ActivityMainBinding
import com.example.myproj.uI.PageAdapter

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.viewPager.adapter = PageAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}