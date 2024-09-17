package com.example.itune.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.itune.R
import com.example.itune.databinding.ActivityTuneBinding
import com.example.itune.db.ResultDatabase
import com.example.itune.repository.TuneRepository

class TuneActivity : AppCompatActivity() {
    lateinit var binding: ActivityTuneBinding
    lateinit var viewModel: TuneViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTuneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = TuneRepository(ResultDatabase(this))
        val viewModelProviderFactory = TuneViewModelProviderFactory(repository)

        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(TuneViewModel::class.java)

        // Get the NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.tuneNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up the BottomNavigationView with NavController
        binding.bottomNavigationView.setupWithNavController(navController)    }
}