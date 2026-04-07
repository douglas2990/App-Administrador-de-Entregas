package com.douglas2990.app_motorista.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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
public final class AutenticacaoMotoristaRepositoryImpl_Factory implements Factory<AutenticacaoMotoristaRepositoryImpl> {
  private final Provider<FirebaseAuth> firebaseAuthProvider;

  private final Provider<FirebaseFirestore> firestoreProvider;

  public AutenticacaoMotoristaRepositoryImpl_Factory(Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseFirestore> firestoreProvider) {
    this.firebaseAuthProvider = firebaseAuthProvider;
    this.firestoreProvider = firestoreProvider;
  }

  @Override
  public AutenticacaoMotoristaRepositoryImpl get() {
    return newInstance(firebaseAuthProvider.get(), firestoreProvider.get());
  }

  public static AutenticacaoMotoristaRepositoryImpl_Factory create(
      Provider<FirebaseAuth> firebaseAuthProvider, Provider<FirebaseFirestore> firestoreProvider) {
    return new AutenticacaoMotoristaRepositoryImpl_Factory(firebaseAuthProvider, firestoreProvider);
  }

  public static AutenticacaoMotoristaRepositoryImpl newInstance(FirebaseAuth firebaseAuth,
      FirebaseFirestore firestore) {
    return new AutenticacaoMotoristaRepositoryImpl(firebaseAuth, firestore);
  }
}
