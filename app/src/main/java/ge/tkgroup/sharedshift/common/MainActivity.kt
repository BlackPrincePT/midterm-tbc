package ge.tkgroup.sharedshift.common

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import ge.tkgroup.sharedshift.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navHostFragment.navController.apply {
            graph = navInflater.inflate(R.navigation.nav_graph)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        enableEdgeToEdge()

        with(binding) {
            setContentView(root)
            ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
                insets
            }

            setSupportActionBar(toolBar)
            bottomMenu.setupWithNavController(navController)
        }

        val appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.authFragment,
                    R.id.registerFragment,
                    R.id.employeesFragment,
                    R.id.equipmentFragment,
                    R.id.settingsFragment
                )
            )
        setupActionBarWithNavController(navController, appBarConfiguration)

        setupNavigationListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupNavigationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomMenu.visibility = when (destination.id) {
                R.id.employeesFragment, R.id.equipmentFragment, R.id.settingsFragment -> View.VISIBLE
                else -> View.GONE
            }
        }
    }
}