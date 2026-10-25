package com.dairum.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dairum.R
import com.dairum.data.security.AppLockManager
import com.dairum.presentation.navigation.DairumNavGraph
import com.dairum.presentation.navigation.Screen
import com.dairum.presentation.screens.lock.LockScreen
import com.dairum.presentation.theme.DairumTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    @Inject
    lateinit var appLockManager: AppLockManager
    
    private var keepSplashScreen = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        Timber.d("MainActivity onCreate")
        
        // Keep splash screen visible while app initializes
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        
        enableEdgeToEdge()
        
        // Handle incoming intents
        val destination = intent.getStringExtra("destination")
        val promptText = intent.getStringExtra("prompt")
        
        setContent {
            // Signal splash screen can dismiss after composition
            LaunchedEffect(Unit) {
                keepSplashScreen = false
            }
            
            DairumTheme {
                DairumAppWithLock(
                    appLockManager = appLockManager,
                    initialDestination = destination,
                    initialPrompt = promptText
                )
            }
        }
    }
}

@Composable
fun DairumAppWithLock(
    appLockManager: AppLockManager,
    initialDestination: String? = null,
    initialPrompt: String? = null
) {
    val isLockConfigured by appLockManager.isLockConfigured().collectAsState(initial = false)
    val biometricEnabled by appLockManager.isBiometricEnabled().collectAsState(initial = false)
    
    var isUnlocked by remember { mutableStateOf(false) }
    var backgroundTimestamp by remember { mutableStateOf(0L) }
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Lock app after 30 seconds in background (grace period for camera/share intents)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    backgroundTimestamp = System.currentTimeMillis()
                }
                Lifecycle.Event.ON_START -> {
                    if (isLockConfigured && isUnlocked && backgroundTimestamp > 0L) {
                        val elapsed = System.currentTimeMillis() - backgroundTimestamp
                        if (elapsed > 30_000L) { // 30 seconds grace period
                            isUnlocked = false
                        }
                    }
                    backgroundTimestamp = 0L
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    // If lock is not configured, show app directly
    val shouldShowLockScreen = isLockConfigured && !isUnlocked
    
    AnimatedVisibility(
        visible = shouldShowLockScreen,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LockScreen(
            appLockManager = appLockManager,
            biometricEnabled = biometricEnabled,
            onUnlocked = { isUnlocked = true }
        )
    }
    
    AnimatedVisibility(
        visible = !shouldShowLockScreen,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        DairumApp(
            initialDestination = initialDestination,
            initialPrompt = initialPrompt
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DairumApp(
    initialDestination: String? = null,
    initialPrompt: String? = null
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Handle initial navigation from widget
    LaunchedEffect(initialDestination) {
        if (initialDestination == "new_entry") {
            Timber.d("Navigating to new entry from widget")
            navController.navigate(Screen.EntryEditor.createRoute())
        }
    }
    
    // List of routes where bottom nav should be shown
    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Gratia.route,
        Screen.GratitudeFolderDetail.route,
        Screen.AffirmationFolderDetail.route,
        Screen.AffirmationPlay.route,
        Screen.Memories.route,
        Screen.Stats.route,
        Screen.More.route
    )
    
    val showBottomBar = currentDestination?.route in bottomNavRoutes
    val showFab = currentDestination?.route == Screen.Home.route
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { 
                            it.route == item.route 
                        } == true || (item.route == Screen.Gratia.route && 
                            currentDestination?.route?.startsWith("gratia") == true)
                        
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.EntryEditor.createRoute())
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.action_add)
                    )
                }
            }
        }
    ) { innerPadding ->
        DairumNavGraph(
            navController = navController,
            innerPadding = innerPadding
        )
    }
}

data class BottomNavItemData(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItemData(
        route = Screen.Home.route,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItemData(
        route = Screen.Gratia.route,
        label = "Gratia",
        selectedIcon = Icons.Filled.SelfImprovement,
        unselectedIcon = Icons.Outlined.SelfImprovement
    ),
    BottomNavItemData(
        route = Screen.Memories.route,
        label = "Memories",
        selectedIcon = Icons.Filled.CollectionsBookmark,
        unselectedIcon = Icons.Outlined.CollectionsBookmark
    ),
    BottomNavItemData(
        route = Screen.Stats.route,
        label = "Stats",
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart
    ),
    BottomNavItemData(
        route = Screen.More.route,
        label = "More",
        selectedIcon = Icons.Filled.MoreHoriz,
        unselectedIcon = Icons.Outlined.MoreHoriz
    )
)
