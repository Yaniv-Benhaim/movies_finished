package tech.gamedev.movietime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import tech.gamedev.movietime.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        findNavController(R.id.nav_host_fragment_activity_main).addOnDestinationChangedListener { _, destination, _ ->

            when(destination.id) {
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications -> {
                    navView.isVisible = true
                    supportActionBar?.show()
                }
                else -> {
                    navView.isVisible = false
                }
            }
        }
    }
}