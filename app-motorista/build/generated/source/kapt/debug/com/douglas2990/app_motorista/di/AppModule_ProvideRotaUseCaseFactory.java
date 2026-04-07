package com.douglas2990.app_motorista.di;

import com.douglas2990.app_motorista.usecase.RotaUseCase;
import com.example.core.repository.IRotaRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideRotaUseCaseFactory implements Factory<RotaUseCase> {
  private final Provider<IRotaRepository> repositoryProvider;

  public AppModule_ProvideRotaUseCaseFactory(Provider<IRotaRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RotaUseCase get() {
    return provideRotaUseCase(repositoryProvider.get());
  }

  public static AppModule_ProvideRotaUseCaseFactory create(
      Provider<IRotaRepository> repositoryProvider) {
    return new AppModule_ProvideRotaUseCaseFactory(repositoryProvider);
  }

  public static RotaUseCase provideRotaUseCase(IRotaRepository repository) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRotaUseCase(repository));
  }
}
