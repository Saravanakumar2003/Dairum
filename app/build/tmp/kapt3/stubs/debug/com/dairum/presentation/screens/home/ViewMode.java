package com.dairum.presentation.screens.home;

import androidx.lifecycle.ViewModel;
import com.dairum.domain.model.Category;
import com.dairum.domain.model.JournalEntry;
import com.dairum.domain.model.Memory;
import com.dairum.domain.model.Person;
import com.dairum.domain.model.Tag;
import com.dairum.domain.repository.CategoryRepository;
import com.dairum.domain.repository.JournalRepository;
import com.dairum.domain.repository.MemoryRepository;
import com.dairum.domain.repository.PersonRepository;
import com.dairum.domain.repository.TagRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.*;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\n"}, d2 = {"Lcom/dairum/presentation/screens/home/ViewMode;", "", "(Ljava/lang/String;I)V", "TIMELINE", "CATEGORY", "TAG", "MOOD", "PEOPLE", "LOCATION", "STORY", "app_debug"})
public enum ViewMode {
    /*public static final*/ TIMELINE /* = new TIMELINE() */,
    /*public static final*/ CATEGORY /* = new CATEGORY() */,
    /*public static final*/ TAG /* = new TAG() */,
    /*public static final*/ MOOD /* = new MOOD() */,
    /*public static final*/ PEOPLE /* = new PEOPLE() */,
    /*public static final*/ LOCATION /* = new LOCATION() */,
    /*public static final*/ STORY /* = new STORY() */;
    
    ViewMode() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.dairum.presentation.screens.home.ViewMode> getEntries() {
        return null;
    }
}