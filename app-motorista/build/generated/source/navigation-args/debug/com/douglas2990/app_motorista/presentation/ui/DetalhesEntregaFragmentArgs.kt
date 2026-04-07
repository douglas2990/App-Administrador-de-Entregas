package com.douglas2990.app_motorista.presentation.ui

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import com.example.core.model.Rota
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Long
import kotlin.Suppress
import kotlin.jvm.JvmStatic

public data class DetalhesEntregaFragmentArgs(
  public val dataSelecionada: Long,
  public val rota: Rota?,
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putLong("dataSelecionada", this.dataSelecionada)
    if (Parcelable::class.java.isAssignableFrom(Rota::class.java)) {
      result.putParcelable("rota", this.rota as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(Rota::class.java)) {
      result.putSerializable("rota", this.rota as Serializable?)
    } else {
      throw UnsupportedOperationException(Rota::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("dataSelecionada", this.dataSelecionada)
    if (Parcelable::class.java.isAssignableFrom(Rota::class.java)) {
      result.set("rota", this.rota as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(Rota::class.java)) {
      result.set("rota", this.rota as Serializable?)
    } else {
      throw UnsupportedOperationException(Rota::class.java.name +
          " must implement Parcelable or Serializable or must be an Enum.")
    }
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): DetalhesEntregaFragmentArgs {
      bundle.setClassLoader(DetalhesEntregaFragmentArgs::class.java.classLoader)
      val __dataSelecionada : Long
      if (bundle.containsKey("dataSelecionada")) {
        __dataSelecionada = bundle.getLong("dataSelecionada")
      } else {
        throw IllegalArgumentException("Required argument \"dataSelecionada\" is missing and does not have an android:defaultValue")
      }
      val __rota : Rota?
      if (bundle.containsKey("rota")) {
        if (Parcelable::class.java.isAssignableFrom(Rota::class.java) ||
            Serializable::class.java.isAssignableFrom(Rota::class.java)) {
          __rota = bundle.get("rota") as Rota?
        } else {
          throw UnsupportedOperationException(Rota::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"rota\" is missing and does not have an android:defaultValue")
      }
      return DetalhesEntregaFragmentArgs(__dataSelecionada, __rota)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle):
        DetalhesEntregaFragmentArgs {
      val __dataSelecionada : Long?
      if (savedStateHandle.contains("dataSelecionada")) {
        __dataSelecionada = savedStateHandle["dataSelecionada"]
        if (__dataSelecionada == null) {
          throw IllegalArgumentException("Argument \"dataSelecionada\" of type long does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"dataSelecionada\" is missing and does not have an android:defaultValue")
      }
      val __rota : Rota?
      if (savedStateHandle.contains("rota")) {
        if (Parcelable::class.java.isAssignableFrom(Rota::class.java) ||
            Serializable::class.java.isAssignableFrom(Rota::class.java)) {
          __rota = savedStateHandle.get<Rota?>("rota")
        } else {
          throw UnsupportedOperationException(Rota::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"rota\" is missing and does not have an android:defaultValue")
      }
      return DetalhesEntregaFragmentArgs(__dataSelecionada, __rota)
    }
  }
}
