package ru.practicum.android.diploma.root.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private val binding: ActivityRootBinding by lazy {
        ActivityRootBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailFragment -> showBottomNavigationBar(
                    isVisible = false
                )

                R.id.similarFragment -> showBottomNavigationBar(
                    isVisible = false
                )

                R.id.filterIndustryFragment -> showBottomNavigationBar(
                    isVisible = false
                )

                R.id.filterRegionFragment -> showBottomNavigationBar(
                    isVisible = false
                )

                R.id.filterCountryFragment -> showBottomNavigationBar(
                    isVisible = false
                )

                R.id.filterPlaceWorkFragment -> showBottomNavigationBar(
                    isVisible = false
                )

                R.id.filterFragment -> showBottomNavigationBar(
                    isVisible = false
                )

                else -> showBottomNavigationBar(isVisible = true)
            }
        }
    }

    private fun showBottomNavigationBar(isVisible: Boolean) {
        val viewVisibility = if (isVisible) View.VISIBLE else View.GONE
        binding.bottomNavigationView.visibility = viewVisibility
    }
}
