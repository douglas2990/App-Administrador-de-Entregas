package com.douglas2990.app_motorista.presentation.ui;

import com.google.firebase.auth.FirebaseAuth;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DetalhesEntregaFragment_MembersInjector implements MembersInjector<DetalhesEntregaFragment> {
  private final Provider<FirebaseAuth> firebaseAuthProvider;

  public DetalhesEntregaFragment_MembersInjector(Provider<FirebaseAuth> firebaseAuthProvider) {
    this.firebaseAuthProvider = firebaseAuthProvider;
  }

  public static MembersInjector<DetalhesEntregaFragment> create(
      Provider<FirebaseAuth> firebaseAuthProvider) {
    return new DetalhesEntregaFragment_MembersInjector(firebaseAuthProvider);
  }

  @Override
  public void injectMembers(DetalhesEntregaFragment instance) {
    injectFirebaseAuth(instance, firebaseAuthProvider.get());
  }

  @InjectedFieldSignature("com.douglas2990.app_motorista.presentation.ui.DetalhesEntregaFragment.firebaseAuth")
  public static void injectFirebaseAuth(DetalhesEntregaFragment instance,
      FirebaseAuth firebaseAuth) {
    instance.firebaseAuth = firebaseAuth;
  }
}
