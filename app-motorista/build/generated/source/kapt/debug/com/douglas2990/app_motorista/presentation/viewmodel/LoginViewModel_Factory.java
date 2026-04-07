package com.douglas2990.app_motorista.presentation.viewmodel;

import com.douglas2990.app_motorista.data.repository.IAutenticacaoMotoristaRepository;
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
public final class LoginViewModel_Factory implements Factory<LoginViewModel> {
  private final Provider<IAutenticacaoMotoristaRepository> repositoryProvider;

  public LoginViewModel_Factory(Provider<IAutenticacaoMotoristaRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public LoginViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static LoginViewModel_Factory create(
      Provider<IAutenticacaoMotoristaRepository> repositoryProvider) {
    return new LoginViewModel_Factory(repositoryProvider);
  }

  public static LoginViewModel newInstance(IAutenticacaoMotoristaRepository repository) {
    return new LoginViewModel(repository);
  }
}
