package com.dairum.data.repository;

import com.dairum.data.local.dao.CategoryDao;
import com.dairum.data.local.entity.CategoryEntity;
import com.dairum.domain.model.Category;
import com.dairum.domain.repository.CategoryRepository;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00100\u000fH\u0016J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00100\u000fH\u0016J\u0018\u0010\u0012\u001a\u0004\u0018\u00010\b2\u0006\u0010\f\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u0013\u001a\u0004\u0018\u00010\bH\u0096@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\b0\u00102\u0006\u0010\u0016\u001a\u00020\u0017H\u0096@\u00a2\u0006\u0002\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020\u0017H\u0096@\u00a2\u0006\u0002\u0010\u001dJ\f\u0010\u001e\u001a\u00020\b*\u00020\u001fH\u0002J\f\u0010 \u001a\u00020\b*\u00020!H\u0002J\f\u0010\"\u001a\u00020\u001f*\u00020\bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/dairum/data/repository/CategoryRepositoryImpl;", "Lcom/dairum/domain/repository/CategoryRepository;", "categoryDao", "Lcom/dairum/data/local/dao/CategoryDao;", "(Lcom/dairum/data/local/dao/CategoryDao;)V", "create", "", "category", "Lcom/dairum/domain/model/Category;", "(Lcom/dairum/domain/model/Category;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllFlow", "Lkotlinx/coroutines/flow/Flow;", "", "getAllWithCountFlow", "getCategoryById", "getDefault", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTopCategories", "limit", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setDefault", "update", "updateSortOrder", "sortOrder", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toDomain", "Lcom/dairum/data/local/entity/CategoryEntity;", "toDomainWithCount", "Lcom/dairum/data/local/entity/CategoryWithCount;", "toEntity", "app_debug"})
public final class CategoryRepositoryImpl implements com.dairum.domain.repository.CategoryRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.CategoryDao categoryDao = null;
    
    @javax.inject.Inject()
    public CategoryRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.CategoryDao categoryDao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object create(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Category category, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object delete(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getCategoryById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.Category> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getDefault(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.Category> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object setDefault(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Category>> getAllFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Category>> getAllWithCountFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTopCategories(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.Category>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateSortOrder(long id, int sortOrder, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.Category toDomain(com.dairum.data.local.entity.CategoryEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.domain.model.Category toDomainWithCount(com.dairum.data.local.entity.CategoryWithCount $this$toDomainWithCount) {
        return null;
    }
    
    private final com.dairum.data.local.entity.CategoryEntity toEntity(com.dairum.domain.model.Category $this$toEntity) {
        return null;
    }
}