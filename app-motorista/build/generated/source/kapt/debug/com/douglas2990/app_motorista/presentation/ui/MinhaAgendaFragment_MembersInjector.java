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
public final class MinhaAgendaFragment_MembersInjector implements MembersInjector<MinhaAgendaFragment> {
  private final Provider<FirebaseAuth> firebaseAuthProvider;

  public MinhaAgendaFragment_MembersInjector(Provider<FirebaseAuth> firebaseAuthProvider) {
    this.firebaseAuthProvider = firebaseAuthProvider;
  }

  public static MembersInjector<MinhaAgendaFragment> create(
      Provider<FirebaseAuth> firebaseAuthProvider) {
    return new MinhaAgendaFragment_MembersInjector(firebaseAuthProvider);
  }

  @Override
  public void injectMembers(MinhaAgendaFragment instance) {
    injectFirebaseAuth(instance, firebaseAuthProvider.get());
  }

  @InjectedFieldSignature("com.douglas2990.app_motorista.presentation.ui.MinhaAgendaFragment.firebaseAuth")
  public static void injectFirebaseAuth(MinhaAgendaFragment instance, FirebaseAuth firebaseAuth) {
    instance.firebaseAuth = firebaseAuth;
  }
}
