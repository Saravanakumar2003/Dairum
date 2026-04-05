package com.dairum.data.repository;

import com.dairum.data.local.dao.PersonDao;
import com.squareup.moshi.Moshi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class PersonRepositoryImpl_Factory implements Factory<PersonRepositoryImpl> {
  private final Provider<PersonDao> personDaoProvider;

  private final Provider<Moshi> moshiProvider;

  public PersonRepositoryImpl_Factory(Provider<PersonDao> personDaoProvider,
      Provider<Moshi> moshiProvider) {
    this.personDaoProvider = personDaoProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public PersonRepositoryImpl get() {
    return newInstance(personDaoProvider.get(), moshiProvider.get());
  }

  public static PersonRepositoryImpl_Factory create(Provider<PersonDao> personDaoProvider,
      Provider<Moshi> moshiProvider) {
    return new PersonRepositoryImpl_Factory(personDaoProvider, moshiProvider);
  }

  public static PersonRepositoryImpl newInstance(PersonDao personDao, Moshi moshi) {
    return new PersonRepositoryImpl(personDao, moshi);
  }
}
