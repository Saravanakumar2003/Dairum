package com.dairum.data.local.dao;

import androidx.room.*;
import com.dairum.data.local.entity.CategoryEntity;
import com.dairum.data.local.entity.CategoryWithCount;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\n\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u000e0\u0010H\'J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u000e0\u0010H\'J\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00072\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00120\u000e2\u0006\u0010\u0018\u001a\u00020\u0016H\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u0016\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\u001b\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u001e\u0010\u001d\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u001e\u001a\u00020\u0016H\u00a7@\u00a2\u0006\u0002\u0010\u001f\u00a8\u0006 "}, d2 = {"Lcom/dairum/data/local/dao/CategoryDao;", "", "clearDefaultFlag", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "category", "Lcom/dairum/data/local/entity/CategoryEntity;", "(Lcom/dairum/data/local/entity/CategoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "", "getAllFlow", "Lkotlinx/coroutines/flow/Flow;", "getAllWithCountFlow", "Lcom/dairum/data/local/entity/CategoryWithCount;", "getById", "getDefault", "getMaxSortOrder", "", "getTopCategories", "limit", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "setAsDefault", "update", "updateSortOrder", "sortOrder", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface CategoryDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.CategoryEntity category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.CategoryEntity category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.entity.CategoryEntity category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM categories WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM categories WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.CategoryEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM categories ORDER BY sortOrder ASC, name ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.CategoryEntity>> getAllFlow();
    
    @androidx.room.Query(value = "SELECT * FROM categories ORDER BY sortOrder ASC, name ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.CategoryEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM categories WHERE isDefault = 1 LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDefault(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.data.local.entity.CategoryEntity> $completion);
    
    @androidx.room.Query(value = "UPDATE categories SET isDefault = 0")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearDefaultFlag(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE categories SET isDefault = 1 WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object setAsDefault(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "\n        SELECT c.*, COUNT(e.id) as entryCount \n        FROM categories c\n        LEFT JOIN journal_entries e ON c.id = e.categoryId AND e.isArchived = 0 AND e.isDraft = 0\n        GROUP BY c.id\n        ORDER BY c.sortOrder ASC, c.name ASC\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.data.local.entity.CategoryWithCount>> getAllWithCountFlow();
    
    @androidx.room.Query(value = "\n        SELECT c.*, COUNT(e.id) as entryCount \n        FROM categories c\n        LEFT JOIN journal_entries e ON c.id = e.categoryId AND e.isArchived = 0 AND e.isDraft = 0\n        GROUP BY c.id\n        ORDER BY entryCount DESC\n        LIMIT :limit\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTopCategories(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.data.local.entity.CategoryWithCount>> $completion);
    
    @androidx.room.Query(value = "SELECT MAX(sortOrder) FROM categories")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMaxSortOrder(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "UPDATE categories SET sortOrder = :sortOrder WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateSortOrder(long id, int sortOrder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}