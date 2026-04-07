package com.douglas2990.d2990entregasv2.data.remote.firebase.repository;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class RotaRepositoryImpl_Factory implements Factory<RotaRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<FirebaseAuth> firebaseAuthProvider;

  private final Provider<FirebaseFirestore> firebaseFirestoreProvider;

  private final Provider<FirebaseStorage> firebaseStorageProvider;

  public RotaRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseFirestore> firebaseFirestoreProvider,
      Provider<FirebaseStorage> firebaseStorageProvider) {
    this.contextProvider = contextProvider;
    this.firebaseAuthProvider = firebaseAuthProvider;
    this.firebaseFirestoreProvider = firebaseFirestoreProvider;
    this.firebaseStorageProvider = firebaseStorageProvider;
  }

  @Override
  public RotaRepositoryImpl get() {
    return newInstance(contextProvider.get(), firebaseAuthProvider.get(), firebaseFirestoreProvider.get(), firebaseStorageProvider.get());
  }

  public static RotaRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<FirebaseAuth> firebaseAuthProvider,
      Provider<FirebaseFirestore> firebaseFirestoreProvider,
      Provider<FirebaseStorage> firebaseStorageProvider) {
    return new RotaRepositoryImpl_Factory(contextProvider, firebaseAuthProvider, firebaseFirestoreProvider, firebaseStorageProvider);
  }

  public static RotaRepositoryImpl newInstance(Context context, FirebaseAuth firebaseAuth,
      FirebaseFirestore firebaseFirestore, FirebaseStorage firebaseStorage) {
    return new RotaRepositoryImpl(context, firebaseAuth, firebaseFirestore, firebaseStorage);
  }
}
