package com.douglas2990.app_motorista.presentation.viewmodel;

import com.douglas2990.app_motorista.usecase.RotaUseCase;
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
public final class AgendaMotoristaViewModel_Factory implements Factory<AgendaMotoristaViewModel> {
  private final Provider<RotaUseCase> rotaUseCaseProvider;

  public AgendaMotoristaViewModel_Factory(Provider<RotaUseCase> rotaUseCaseProvider) {
    this.rotaUseCaseProvider = rotaUseCaseProvider;
  }

  @Override
  public AgendaMotoristaViewModel get() {
    return newInstance(rotaUseCaseProvider.get());
  }

  public static AgendaMotoristaViewModel_Factory create(Provider<RotaUseCase> rotaUseCaseProvider) {
    return new AgendaMotoristaViewModel_Factory(rotaUseCaseProvider);
  }

  public static AgendaMotoristaViewModel newInstance(RotaUseCase rotaUseCase) {
    return new AgendaMotoristaViewModel(rotaUseCase);
  }
}
