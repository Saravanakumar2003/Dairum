
# Dairum - Personal Journal App

Dairum is an Android application designed to provide users with a seamless and intuitive journaling experience. The app allows users to create, organize, and reflect on their daily thoughts, experiences, and memories in a digital format. With a user-friendly interface and a variety of features, Dairum aims to help users cultivate a consistent journaling habit and promote self-reflection.

## Features

### Journal Entries
- **Rich Text Entries**: Create daily journal entries with text formatting (bold, italic, underline, headings, lists)
- **Title & Content**: Add optional titles and detailed content to organize your thoughts
- **Templates**: Use built-in or custom templates for quick entry creation (gratitude, daily reflection, etc.)
- **Entry Categories**: Organize entries into customizable color-coded categories
- **Draft Support**: Save entries as drafts to finish later
- **Date Editing**: Change entry timestamps as needed

### Media & Attachments
- **Photos**: Attach multiple photos to your entries
- **Videos**: Add video recordings to capture moments
- **Voice Notes**: Record or attach audio files directly in entries
- **File Attachments**: Attach documents and other files
- **Media Gallery**: Browse all your attached media in the Files tab organized by type (Photos, Videos, Voice Notes, Attachments)

### Mood Tracking
- **Extended Mood Options**: Choose from 30 different moods across 4 categories:
  - 😊 Positive (Happy, Amazing, Excited, Grateful, Love, Peaceful, Hopeful, Proud, Content, Inspired, Motivated)
  - 😐 Neutral (Calm, Thoughtful, Tired, Bored)
  - 😔 Negative (Sad, Anxious, Stressed, Angry, Frustrated, Fearful, Lonely, Disappointed, Overwhelmed, Depressed)
  - 🤔 Mixed (Confused, Nervous, Surprised, Nostalgic)
- **Mood Statistics**: View mood distribution, top moods, and category breakdown

### Tags & People
- **Custom Tags**: Create and manage tags to categorize and find entries
- **People Tracking**: Tag people mentioned in your entries for relationship journaling
- **Quick Search**: Find entries by tag or person

### Custom Trackers
- **Multiple Tracker Types**: Track various metrics in your entries:
  - Number trackers (with optional units)
  - Text trackers
  - Boolean (yes/no) trackers
  - Rating trackers (1-5 stars)
  - Weight trackers
  - Select from options trackers
- **Activity Tracking**: Track habits, activities, or any custom metrics over time

### Location & Weather
- **Auto Location**: Automatically capture your location for new entries
- **Manual Location**: Search and select locations from a map
- **Location Map View**: See all your entries on a map
- **Weather Integration**: Automatic weather data capture for your entries

### Memories & Stories
- **On This Day**: See entries from the same day in previous years
- **Memory Collections**: Create custom memory collections
- **Anniversary Reminders**: Get notified about special entry anniversaries

### Files & Media Management
- **Organized Gallery**: Browse all media by type (Photos, Videos, Voice Notes, Attachments)
- **Download Files**: Save attachments to Documents/dairum folder
- **Location Map**: View all entries on an interactive map
- **Entry Navigation**: Quickly jump to entries from the files view

### Statistics & Insights
- **Entry Statistics**: View total entries, word counts, and writing streaks
- **Mood Analysis**: See mood distribution across categories with percentages
- **Top Moods**: View your most frequently logged moods
- **Tag Usage**: See which tags you use most often
- **People Mentions**: Track who appears most in your entries
- **Activity Patterns**: View hourly and daily writing patterns

### Export & Backup
- **PDF Export**: Export entries to formatted PDF with optional media
- **Text Export**: Export entries as plain text files
- **Full Backup**: Create complete backups including database and media
- **Restore**: Restore from backups with merge or replace options
- **Export History**: Track all exports with share functionality
- **Default Location**: Exports save to Documents/dairum folder

### Automation Integration
- **MacroDroid/Tasker Support**: Automate exports and backups
- **Scheduled Exports**: Configure automatic periodic exports
- **Custom Actions**: Define export parameters (format, date range, include media)

### Security
- **App Lock**: Protect your journal with biometric or PIN authentication
- **Privacy First**: All data stored locally on your device

### Customization
- **Categories**: Create unlimited custom categories with colors and icons
- **Tags**: Organize with custom tags
- **Templates**: Define reusable entry templates
- **Trackers**: Create custom metric trackers
- **Theme Options**: Light and dark mode support

### Notifications & Reminders
- **Daily Reminders**: Get reminded to write in your journal
- **Mood Check-ins**: Periodic prompts to log your mood
- **Memory Notifications**: "On This Day" anniversary reminders
- **Smart Reminders**: Intelligent reminder scheduling

### Writing Prompts
- **Daily Prompts**: Get a new writing prompt every day to inspire journaling
- **12 Categories**: Reflection, Gratitude, Goals, Creativity, Relationships, Self-Discovery, Mindfulness, Growth, Memories, Future, Daily Life, Emotions
- **Seasonal Prompts**: Special prompts for different seasons
- **Special Occasions**: Themed prompts for New Year, weekends, Mondays, Fridays
- **Mood-Based**: Prompts related to how you're feeling
- **Refresh Option**: Get a new random prompt anytime

### Home Screen Widget
- **Quick Entry Widget**: Add the widget to your home screen for quick access
- **Daily Prompt Display**: See the daily writing prompt on your home screen
- **One-Tap Entry**: Create a new entry directly from the widget
- **Use Prompt**: Start writing with the displayed prompt
- **Refresh**: Get a new writing prompt with one tap

### UI Enhancements
- **Shimmer Loading**: Beautiful loading animations while content loads
- **Empty States**: Helpful illustrations and messages when screens are empty
- **Swipe Actions**: Swipe entries to favorite or archive them
- **Haptic Feedback**: Tactile feedback for interactions
- **Splash Screen**: Branded splash screen on app launch

### AI Features (Fully Offline)
- **AI Chat**: Have intelligent conversations about your journal entries
- **Mood Insights**: Get AI-powered analysis of mood patterns and trends
- **Entry Summaries**: Automatic summarization of journal entries
- **Semantic Search**: Find entries based on meaning, not just keywords
- **Pattern Recognition**: Discover writing patterns and themes across entries
- **100% Offline**: All AI processing runs locally on your device - no data ever leaves your phone

#### Supported AI Models
- **LLM Inference**: Uses llama.cpp for running GGUF format language models
- **Embeddings**: ONNX Runtime for semantic embeddings
- **Recommended Models**:
  - Qwen2.5-1.5B-Instruct-GGUF (Q4_K_M) - Good balance of speed and quality
  - SmolLM2-1.7B-Instruct-GGUF - Lightweight alternative
  - Any GGUF model compatible with llama.cpp

## Technical Architecture

### Technologies Used
- **Language**: Kotlin 1.9.22
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Database**: Room
- **Networking**: Retrofit + OkHttp + Moshi
- **Image Loading**: Coil
- **Media**: Media3/ExoPlayer
- **Maps**: Google Maps + OSMDroid
- **Charts**: Compose UI
- **Background Work**: WorkManager
- **DataStore**: Preferences DataStore
- **Security**: Biometric + EncryptedSharedPreferences
- **Logging**: Timber
- **PDF Generation**: Android `PdfDocument`
- **AI/LLM**: llama.cpp (native JNI) + ONNX Runtime

### AI Architecture
The AI system is designed with a modular, layered architecture:

```
┌─────────────────────────────────────────────┐
│           AI Use Cases Layer                │
│  (JournalAnalysis, Chat, Insights)          │
├─────────────────────────────────────────────┤
│         Local LLM Layer (llama.cpp)         │
│  (Native JNI bridge for on-device inference)│
├─────────────────────────────────────────────┤
│           Embedding Layer (ONNX)            │
│  (Semantic vector embeddings)               │
├─────────────────────────────────────────────┤
│        Model Management Layer               │
│  (Import, validate, store GGUF models)      │
└─────────────────────────────────────────────┘
```

### Building with AI Support

The native llama.cpp library is now integrated. To build:

```bash
# 1. Clone llama.cpp sources (one-time setup)
./scripts/setup_llama.sh

# 2. Build the project
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew assembleDebug --no-daemon
```

**If you get "Subprocess killed" errors with ninja:**
```bash
# Fix by replacing corrupted SDK ninja with Homebrew version
brew install ninja
mv ~/Library/Android/sdk/cmake/3.22.1/bin/ninja ~/Library/Android/sdk/cmake/3.22.1/bin/ninja.bak
ln -s $(which ninja) ~/Library/Android/sdk/cmake/3.22.1/bin/ninja
```

Note: The first build with native code takes longer as it compiles llama.cpp for ARM64 and x86_64.

### Icon Attribution

The icons used in this project are sourced from Flaticon, a popular platform for free icons. The specific icons utilized in this project are categorized under "Diary icons" and were created by the designer "ranksol graphics".

<a href="https://www.flaticon.com/free-icons/diary" title="diary icons">Diary icons created by ranksol graphics - Flaticon</a>


### Build Command

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" && ./gradlew assembleDebug --no-daemon 2>&1
```