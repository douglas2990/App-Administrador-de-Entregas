package com.douglas2990.app_motorista.presentation.viewmodel;

import com.example.core.repository.IRotaRepository;
import com.google.firebase.auth.FirebaseAuth;
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
public final class RotasMotoristaViewModel_Factory implements Factory<RotasMotoristaViewModel> {
  private final Provider<IRotaRepository> rotaRepositoryProvider;

  private final Provider<FirebaseAuth> authProvider;

  public RotasMotoristaViewModel_Factory(Provider<IRotaRepository> rotaRepositoryProvider,
      Provider<FirebaseAuth> authProvider) {
    this.rotaRepositoryProvider = rotaRepositoryProvider;
    this.authProvider = authProvider;
  }

  @Override
  public RotasMotoristaViewModel get() {
    return newInstance(rotaRepositoryProvider.get(), authProvider.get());
  }

  public static RotasMotoristaViewModel_Factory create(
      Provider<IRotaRepository> rotaRepositoryProvider, Provider<FirebaseAuth> authProvider) {
    return new RotasMotoristaViewModel_Factory(rotaRepositoryProvider, authProvider);
  }

  public static RotasMotoristaViewModel newInstance(IRotaRepository rotaRepository,
      FirebaseAuth auth) {
    return new RotasMotoristaViewModel(rotaRepository, auth);
  }
}
