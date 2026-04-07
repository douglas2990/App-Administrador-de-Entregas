package com.douglas2990.app_motorista.presentation.ui

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Long
import kotlin.jvm.JvmStatic

public data class RotasMotoristaFragmentArgs(
  public val dataSelecionada: Long,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putLong("dataSelecionada", this.dataSelecionada)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("dataSelecionada", this.dataSelecionada)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): RotasMotoristaFragmentArgs {
      bundle.setClassLoader(RotasMotoristaFragmentArgs::class.java.classLoader)
      val __dataSelecionada : Long
      if (bundle.containsKey("dataSelecionada")) {
        __dataSelecionada = bundle.getLong("dataSelecionada")
      } else {
        throw IllegalArgumentException("Required argument \"dataSelecionada\" is missing and does not have an android:defaultValue")
      }
      return RotasMotoristaFragmentArgs(__dataSelecionada)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle):
        RotasMotoristaFragmentArgs {
      val __dataSelecionada : Long?
      if (savedStateHandle.contains("dataSelecionada")) {
        __dataSelecionada = savedStateHandle["dataSelecionada"]
        if (__dataSelecionada == null) {
          throw IllegalArgumentException("Argument \"dataSelecionada\" of type long does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"dataSelecionada\" is missing and does not have an android:defaultValue")
      }
      return RotasMotoristaFragmentArgs(__dataSelecionada)
    }
  }
}
