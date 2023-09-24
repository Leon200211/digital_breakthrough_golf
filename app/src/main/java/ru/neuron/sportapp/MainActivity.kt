package ru.neuron.sportapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.kotlinx.dl.example.app.MainCameraActivity
import ru.neuron.sportapp.home.HomeViewModel
import ru.neuron.sportapp.home.SportHome
import ru.neuron.sportapp.topappbarmenu.TopAppBarMenu
import ru.neuron.sportapp.ui.SportTheme

class MainActivity: ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private fun allPermissionsGranted() = MainActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        init {
            System.loadLibrary("opencv_java4")
        }
        val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 10)
        }
        setContent {
            SportTheme {
                val context = LocalContext.current
                val scaffoldState = rememberScaffoldState()
                ModalNavigationDrawer(
                    drawerContent = {
                        Surface(
                            Modifier
                                .fillMaxHeight()
                                .defaultMinSize(minWidth = 10.dp)) {
                            val columnPadding = PaddingValues(10.dp)
                            Column {
                                Text(
                                    modifier = Modifier.padding(columnPadding),
                                    text = "Hi)", style = MaterialTheme.typography.button)
                            }
                        }

                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBarMenu(label="МЕНЮ")
                        }
                    ) { paddingValues ->
                    paddingValues
                        AppNavigation(
                            homeViewModel = homeViewModel,
                            modifier = Modifier.padding(paddingValues),
                        )
                    }
                    
                }
            }
        }
    }
}
@Composable
fun AppNavigation(homeViewModel: HomeViewModel,
                  modifier: Modifier = Modifier,
                  ) {
    val context = LocalContext.current
    NavHost(
        modifier = modifier,
        navController = rememberNavController(),
        startDestination = Routes.Home.route
    ) {

        composable(Routes.Home.route) {
            SportHome(homeViewModel)
        }
    }
}
sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Training : Routes("training")
    object CameraTraining : Routes("camera_training")
}

