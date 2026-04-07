package com.douglas2990.app_motorista.usecase;

import com.example.core.repository.IRotaRepository;
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
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class RotaUseCase_Factory implements Factory<RotaUseCase> {
  private final Provider<IRotaRepository> repositoryProvider;

  public RotaUseCase_Factory(Provider<IRotaRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RotaUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static RotaUseCase_Factory create(Provider<IRotaRepository> repositoryProvider) {
    return new RotaUseCase_Factory(repositoryProvider);
  }

  public static RotaUseCase newInstance(IRotaRepository repository) {
    return new RotaUseCase(repository);
  }
}
