package com.dairum.presentation.navigation

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    // Main tabs
    object Home : Screen("home")
    object AI : Screen("ai")
    object Memories : Screen("memories")
    object More : Screen("more")
    object Files : Screen("files")
    object Stats : Screen("stats")
    object Settings : Screen("settings")
    
    // Journal
    object EntryDetail : Screen("entry/{entryId}") {
        fun createRoute(entryId: Long) = "entry/$entryId"
    }
    object EntryEditor : Screen("editor?entryId={entryId}") {
        fun createRoute(entryId: Long? = null) = if (entryId != null) {
            "editor?entryId=$entryId"
        } else {
            "editor"
        }
    }
    
    // Memory Detail
    object MemoryDetail : Screen("memory/{memoryId}") {
        fun createRoute(memoryId: Long) = "memory/$memoryId"
    }
    
    // Views
    object CategoryView : Screen("category/{categoryId}") {
        fun createRoute(categoryId: Long) = "category/$categoryId"
    }
    object TagView : Screen("tag/{tagId}") {
        fun createRoute(tagId: Long) = "tag/$tagId"
    }
    object MoodView : Screen("mood/{moodLevel}") {
        fun createRoute(moodLevel: Int) = "mood/$moodLevel"
    }
    object StoryView : Screen("story")
    
    // Files sub-screens
    object PhotoViewer : Screen("photo/{mediaId}") {
        fun createRoute(mediaId: Long) = "photo/$mediaId"
    }
    object VideoPlayer : Screen("video/{mediaId}") {
        fun createRoute(mediaId: Long) = "video/$mediaId"
    }
    object LocationMap : Screen("locations")
    
    // Favorites
    object Favorites : Screen("favorites")
    
    // Settings sub-screens
    object ManageCategories : Screen("more/categories")
    object ManageTags : Screen("more/tags")
    object ManageTrackers : Screen("more/trackers")
    object ManagePeople : Screen("more/people")
    object ManageTemplates : Screen("settings/templates")
    object ExportSettings : Screen("settings/export")
    object BackupRestore : Screen("settings/backup")
    object TaskerIntegration : Screen("settings/automation")
    object SecuritySettings : Screen("settings/security")
    object NotificationSettings : Screen("settings/notifications")
    object ApiKeySettings : Screen("settings/api-keys")
    object AboutScreen : Screen("settings/about")
    
    // Search
    object Search : Screen("search")
    
    // AI sub-screens (accessed from More)
    object AIChat : Screen("ai/chat")
    object AIReflections : Screen("ai/reflections")
    object AIPatterns : Screen("ai/patterns")
    object AISettings : Screen("ai/settings")
}

/**
 * Bottom navigation items
 */
enum class BottomNavItem(
    val screen: Screen,
    val icon: String,
    val label: String
) {
    HOME(Screen.Home, "home", "Home"),
    FILES(Screen.Files, "folder", "Files"),
    MEMORIES(Screen.Memories, "collections_bookmark", "Memories"),
    STATS(Screen.Stats, "bar_chart", "Stats"),
    MORE(Screen.More, "more_horiz", "More")
}
