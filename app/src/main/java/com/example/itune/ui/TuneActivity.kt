package com.example.itune.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.itune.R
import com.example.itune.databinding.ActivityTuneBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class TuneActivity : AppCompatActivity() {
    lateinit var bottomNavigation: BottomNavigationView
    lateinit var fragment: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tune)
        bottomNavigation = findViewById(R.id.bottomNavigationView)
        fragment = findViewById(R.id.tuneNavHostFragment)
        bottomNavigation.setupWithNavController(fragment.findNavController())
    }
}