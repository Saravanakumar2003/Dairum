package com.dairum.data.repository;

import com.dairum.data.local.dao.PersonDao;
import com.dairum.data.local.entity.PersonEntity;
import com.dairum.domain.model.ImportantDate;
import com.dairum.domain.model.Person;
import com.dairum.domain.repository.PersonRepository;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\rH\u0096@\u00a2\u0006\u0002\u0010\u0014J\u0014\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\t0\u0016H\u0016J\u0014\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\t0\u0016H\u0016J\u0018\u0010\u0018\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0013\u001a\u00020\rH\u0096@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000f0\t2\u0006\u0010\u001a\u001a\u00020\rH\u0096@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u000f0\t2\u0006\u0010\u001c\u001a\u00020\u001dH\u0096@\u00a2\u0006\u0002\u0010\u001eJ\u0018\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\t2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0002J$\u0010#\u001a\b\u0012\u0004\u0012\u00020\u000f0\t2\u0006\u0010$\u001a\u00020\"2\u0006\u0010\u001c\u001a\u00020\u001dH\u0096@\u00a2\u0006\u0002\u0010%J\u0018\u0010&\u001a\u0004\u0018\u00010\"2\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020 0\tH\u0002J\u001e\u0010(\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\r2\u0006\u0010)\u001a\u00020*H\u0096@\u00a2\u0006\u0002\u0010+J\u0016\u0010,\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@\u00a2\u0006\u0002\u0010\u0010J\f\u0010-\u001a\u00020\u000f*\u00020.H\u0002J\f\u0010/\u001a\u00020.*\u00020\u000fH\u0002RJ\u0010\u0007\u001a>\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\n \u000b*\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t0\t \u000b*\u001e\u0012\u0018\u0012\u0016\u0012\u0004\u0012\u00020\n \u000b*\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t0\t\u0018\u00010\b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/dairum/data/repository/PersonRepositoryImpl;", "Lcom/dairum/domain/repository/PersonRepository;", "personDao", "Lcom/dairum/data/local/dao/PersonDao;", "moshi", "Lcom/squareup/moshi/Moshi;", "(Lcom/dairum/data/local/dao/PersonDao;Lcom/squareup/moshi/Moshi;)V", "importantDatesAdapter", "Lcom/squareup/moshi/JsonAdapter;", "", "Lcom/dairum/data/repository/ImportantDateJson;", "kotlin.jvm.PlatformType", "create", "", "person", "Lcom/dairum/domain/model/Person;", "(Lcom/dairum/domain/model/Person;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllFlow", "Lkotlinx/coroutines/flow/Flow;", "getAllWithCountFlow", "getById", "getPeopleForEntry", "entryId", "getTopPeople", "limit", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseImportantDates", "Lcom/dairum/domain/model/ImportantDate;", "json", "", "search", "query", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "serializeImportantDates", "dates", "toggleFavorite", "isFavorite", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "toDomain", "Lcom/dairum/data/local/entity/PersonEntity;", "toEntity", "app_debug"})
public final class PersonRepositoryImpl implements com.dairum.domain.repository.PersonRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.dairum.data.local.dao.PersonDao personDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.squareup.moshi.Moshi moshi = null;
    private final com.squareup.moshi.JsonAdapter<java.util.List<com.dairum.data.repository.ImportantDateJson>> importantDatesAdapter = null;
    
    @javax.inject.Inject()
    public PersonRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.dairum.data.local.dao.PersonDao personDao, @org.jetbrains.annotations.NotNull()
    com.squareup.moshi.Moshi moshi) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object create(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Person person, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.dairum.domain.model.Person person, @org.jetbrains.annotations.NotNull()
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
    public java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.dairum.domain.model.Person> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Person>> getAllFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.dairum.domain.model.Person>> getAllWithCountFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object search(@org.jetbrains.annotations.NotNull()
    java.lang.String query, int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.Person>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTopPeople(int limit, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.Person>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getPeopleForEntry(long entryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.dairum.domain.model.Person>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object toggleFavorite(long id, boolean isFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.dairum.domain.model.Person toDomain(com.dairum.data.local.entity.PersonEntity $this$toDomain) {
        return null;
    }
    
    private final com.dairum.data.local.entity.PersonEntity toEntity(com.dairum.domain.model.Person $this$toEntity) {
        return null;
    }
    
    private final java.util.List<com.dairum.domain.model.ImportantDate> parseImportantDates(java.lang.String json) {
        return null;
    }
    
    private final java.lang.String serializeImportantDates(java.util.List<com.dairum.domain.model.ImportantDate> dates) {
        return null;
    }
}