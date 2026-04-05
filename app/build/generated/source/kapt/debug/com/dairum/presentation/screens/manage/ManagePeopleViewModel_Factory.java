package com.dairum.presentation.screens.manage;

import com.dairum.domain.repository.PersonRepository;
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
public final class ManagePeopleViewModel_Factory implements Factory<ManagePeopleViewModel> {
  private final Provider<PersonRepository> personRepositoryProvider;

  public ManagePeopleViewModel_Factory(Provider<PersonRepository> personRepositoryProvider) {
    this.personRepositoryProvider = personRepositoryProvider;
  }

  @Override
  public ManagePeopleViewModel get() {
    return newInstance(personRepositoryProvider.get());
  }

  public static ManagePeopleViewModel_Factory create(
      Provider<PersonRepository> personRepositoryProvider) {
    return new ManagePeopleViewModel_Factory(personRepositoryProvider);
  }

  public static ManagePeopleViewModel newInstance(PersonRepository personRepository) {
    return new ManagePeopleViewModel(personRepository);
  }
}
