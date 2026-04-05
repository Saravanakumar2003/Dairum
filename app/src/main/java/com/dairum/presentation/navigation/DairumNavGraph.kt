package com.dairum.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dairum.presentation.screens.category.CategoryEntriesScreen
import com.dairum.presentation.screens.entry.EntryViewScreen
import com.dairum.presentation.screens.editor.EditorScreen
import com.dairum.presentation.screens.favorites.FavoritesScreen
import com.dairum.presentation.screens.files.FilesScreen
import com.dairum.presentation.screens.files.PhotoViewerScreen
import com.dairum.presentation.screens.home.HomeScreen
import com.dairum.presentation.screens.manage.*
import com.dairum.presentation.screens.map.LocationMapScreen
import com.dairum.presentation.screens.more.MoreScreen
import com.dairum.presentation.screens.settings.AboutScreen
import com.dairum.presentation.screens.settings.ApiKeySettingsScreen
import com.dairum.presentation.screens.settings.BackupRestoreScreen
import com.dairum.presentation.screens.settings.ManualExportScreen
import com.dairum.presentation.screens.settings.NotificationSettingsScreen
import com.dairum.presentation.screens.settings.SecuritySettingsScreen
import com.dairum.presentation.screens.settings.SettingsScreen
import com.dairum.presentation.screens.settings.TaskerIntegrationScreen
import com.dairum.presentation.screens.settings.TemplateSettingsScreen
import com.dairum.presentation.screens.memories.MemoriesScreen
import com.dairum.presentation.screens.memories.MemoriesViewModel
import com.dairum.presentation.screens.memories.MemoryDetailScreen
import com.dairum.presentation.screens.stats.StatsScreen
import com.dairum.presentation.screens.story.StoryScreen
import com.dairum.presentation.screens.tag.TagEntriesScreen
import com.dairum.presentation.screens.chat.ChatScreen
import com.dairum.presentation.screens.ai.AITab
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DairumNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier.padding(innerPadding),
        enterTransition = {
            fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        }
    ) {
        // Main tabs
        composable(Screen.Home.route) {
            val memoriesViewModel: MemoriesViewModel = hiltViewModel()
            HomeScreen(
                onEntryClick = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                },
                onNewEntry = {
                    navController.navigate(Screen.EntryEditor.createRoute())
                },
                onCategoryClick = { categoryId ->
                    navController.navigate(Screen.CategoryView.createRoute(categoryId))
                },
                onTagClick = { tagId ->
                    navController.navigate(Screen.TagView.createRoute(tagId))
                },
                onMoodClick = { moodLevel ->
                    navController.navigate(Screen.MoodView.createRoute(moodLevel))
                },
                onStoryClick = {
                    navController.navigate(Screen.StoryView.route)
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onCreateMemoryWithEntries = { title, description, entryIds ->
                    memoriesViewModel.createMemory(
                        title = title,
                        description = description,
                        startDate = java.time.LocalDateTime.now(),
                        endDate = java.time.LocalDateTime.now(),
                        entryIds = entryIds
                    )
                }
            )
        }
        
        // AI tab (legacy route - redirects to chat)
        composable(Screen.AI.route) {
            com.dairum.presentation.screens.ai.AIScreen()
        }
        
        // AI Chat - Memory-aware conversation
        composable(Screen.AIChat.route) {
            ChatScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToModels = { navController.navigate(Screen.AISettings.route) }
            )
        }
        
        // AI Reflections
        composable(Screen.AIReflections.route) {
            com.dairum.presentation.screens.ai.AIReflectionsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // AI Patterns
        composable(Screen.AIPatterns.route) {
            com.dairum.presentation.screens.ai.AIPatternsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // AI Settings (Models)
        composable(Screen.AISettings.route) {
            com.dairum.presentation.screens.ai.AISettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Memories tab
        composable(Screen.Memories.route) {
            MemoriesScreen(
                onMemoryClick = { memoryId ->
                    navController.navigate(Screen.MemoryDetail.createRoute(memoryId))
                },
                onEntryClick = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                }
            )
        }
        
        // Memory Detail
        composable(
            route = Screen.MemoryDetail.route,
            arguments = listOf(
                navArgument("memoryId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val memoryId = backStackEntry.arguments?.getLong("memoryId") ?: return@composable
            MemoryDetailScreen(
                memoryId = memoryId,
                onBackClick = { navController.popBackStack() },
                onEntryClick = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                },
                onPhotoClick = { mediaId ->
                    navController.navigate(Screen.PhotoViewer.createRoute(mediaId))
                }
            )
        }
        
        composable(Screen.Files.route) {
            FilesScreen(
                onPhotoClick = { mediaId ->
                    navController.navigate(Screen.PhotoViewer.createRoute(mediaId))
                },
                onVideoClick = { mediaId ->
                    navController.navigate(Screen.VideoPlayer.createRoute(mediaId))
                },
                onEntryClick = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                },
                onLocationMapClick = {
                    navController.navigate(Screen.LocationMap.route)
                }
            )
        }
        
        composable(Screen.Stats.route) {
            StatsScreen()
        }
        
        composable(Screen.More.route) {
            MoreScreen(
                onNavigateToFiles = {
                    navController.navigate(Screen.Files.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToLocationMap = {
                    navController.navigate(Screen.LocationMap.route)
                },
                onNavigateToCategories = {
                    navController.navigate(Screen.ManageCategories.route)
                },
                onNavigateToTags = {
                    navController.navigate(Screen.ManageTags.route)
                },
                onNavigateToTrackers = {
                    navController.navigate(Screen.ManageTrackers.route)
                },
                onNavigateToPeopleDirectory = {
                    navController.navigate(Screen.ManagePeople.route)
                },
                onNavigateToFavorites = {
                    navController.navigate(Screen.Favorites.route)
                },
                onNavigateToAIChat = {
                    navController.navigate(Screen.AIChat.route)
                },
                onNavigateToAIReflections = {
                    navController.navigate(Screen.AIReflections.route)
                },
                onNavigateToAIPatterns = {
                    navController.navigate(Screen.AIPatterns.route)
                },
                onNavigateToAISettings = {
                    navController.navigate(Screen.AISettings.route)
                }
            )
        }
        
        // Favorites
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() },
                onEntryClick = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onManageTemplates = {
                    navController.navigate(Screen.ManageTemplates.route)
                },
                onExportSettings = {
                    navController.navigate(Screen.ExportSettings.route)
                },
                onBackupRestore = {
                    navController.navigate(Screen.BackupRestore.route)
                },
                onTaskerIntegration = {
                    navController.navigate(Screen.TaskerIntegration.route)
                },
                onSecuritySettings = {
                    navController.navigate(Screen.SecuritySettings.route)
                },
                onNotificationSettings = {
                    navController.navigate(Screen.NotificationSettings.route)
                },
                onApiKeySettings = {
                    navController.navigate(Screen.ApiKeySettings.route)
                },
                onAbout = {
                    navController.navigate(Screen.AboutScreen.route)
                }
            )
        }
        
        // Entry detail
        composable(
            route = Screen.EntryDetail.route,
            arguments = listOf(
                navArgument("entryId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getLong("entryId") ?: return@composable
            EntryViewScreen(
                entryId = entryId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(Screen.EntryEditor.createRoute(id)) }
            )
        }
        
        // Entry editor
        composable(
            route = Screen.EntryEditor.route,
            arguments = listOf(
                navArgument("entryId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getLong("entryId")?.takeIf { it != -1L }
            EditorScreen(
                entryId = entryId,
                onBackClick = { navController.popBackStack() },
                onEntrySaved = { navController.popBackStack() }
            )
        }
        
        // Story view
        composable(Screen.StoryView.route) {
            StoryScreen(
                onEntryClick = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                },
                onNavigateUp = { navController.popBackStack() }
            )
        }
        
        // Category view
        composable(
            route = Screen.CategoryView.route,
            arguments = listOf(
                navArgument("categoryId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: return@composable
            CategoryEntriesScreen(
                categoryId = categoryId,
                onEntryClick = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Tag view
        composable(
            route = Screen.TagView.route,
            arguments = listOf(
                navArgument("tagId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val tagId = backStackEntry.arguments?.getLong("tagId") ?: return@composable
            TagEntriesScreen(
                tagId = tagId,
                onEntryClick = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Mood view  
        composable(
            route = Screen.MoodView.route,
            arguments = listOf(
                navArgument("moodLevel") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val moodLevel = backStackEntry.arguments?.getInt("moodLevel") ?: return@composable
            // MoodEntriesScreen(
            //     moodLevel = moodLevel,
            //     onEntryClick = { entryId ->
            //         navController.navigate(Screen.EntryDetail.createRoute(entryId))
            //     },
            //     onBackClick = { navController.popBackStack() }
            // )
        }
        
        // Search
        composable(Screen.Search.route) {
            // SearchScreen(
            //     onEntryClick = { entryId ->
            //         navController.navigate(Screen.EntryDetail.createRoute(entryId))
            //     },
            //     onBackClick = { navController.popBackStack() }
            // )
        }
        
        // Settings sub-screens
        composable(Screen.ManageCategories.route) {
            ManageCategoriesScreen(onBackClick = { navController.popBackStack() })
        }
        
        composable(Screen.ManageTags.route) {
            ManageTagsScreen(onBackClick = { navController.popBackStack() })
        }
        
        composable(Screen.ManageTrackers.route) {
            ManageTrackersScreen(onBackClick = { navController.popBackStack() })
        }
        
        composable(Screen.ManagePeople.route) {
            ManagePeopleScreen(onBackClick = { navController.popBackStack() })
        }
        
        composable(Screen.ManageTemplates.route) {
            TemplateSettingsScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Screen.ExportSettings.route) {
            ManualExportScreen(onBackClick = { navController.popBackStack() })
        }
        
        composable(Screen.BackupRestore.route) {
            BackupRestoreScreen(onBackClick = { navController.popBackStack() })
        }
        
        composable(Screen.TaskerIntegration.route) {
            TaskerIntegrationScreen(onBackClick = { navController.popBackStack() })
        }
        
        composable(Screen.SecuritySettings.route) {
            SecuritySettingsScreen(
                onNavigateUp = { navController.popBackStack() }
            )
        }
        
        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(
                onNavigateUp = { navController.popBackStack() }
            )
        }
        
        composable(Screen.ApiKeySettings.route) {
            ApiKeySettingsScreen(
                onNavigateUp = { navController.popBackStack() }
            )
        }
        
        composable(Screen.AboutScreen.route) {
            AboutScreen(onBackClick = { navController.popBackStack() })
        }
        
        // Media viewers
        composable(
            route = Screen.PhotoViewer.route,
            arguments = listOf(
                navArgument("mediaId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val mediaId = backStackEntry.arguments?.getLong("mediaId") ?: return@composable
            PhotoViewerScreen(
                mediaId = mediaId,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(
                navArgument("mediaId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val mediaId = backStackEntry.arguments?.getLong("mediaId") ?: return@composable
            // VideoPlayerScreen(
            //     mediaId = mediaId,
            //     onBackClick = { navController.popBackStack() }
            // )
        }
        
        composable(Screen.LocationMap.route) {
            LocationMapScreen(
                onEntryClick = { entryId ->
                    navController.navigate(Screen.EntryDetail.createRoute(entryId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
