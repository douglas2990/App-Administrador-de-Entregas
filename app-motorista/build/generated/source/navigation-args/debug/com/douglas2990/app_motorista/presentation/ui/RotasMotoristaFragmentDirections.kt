package com.douglas2990.app_motorista.presentation.ui

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import com.douglas2990.app_motorista.R
import com.example.core.model.Rota
import java.io.Serializable
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.Long
import kotlin.Suppress

public class RotasMotoristaFragmentDirections private constructor() {
  private data class ActionRotasMotoristaFragmentToDetalhesEntregaFragment(
    public val dataSelecionada: Long,
    public val rota: Rota?,
  ) : NavDirections {
    public override val actionId: Int =
        R.id.action_rotasMotoristaFragment_to_detalhesEntregaFragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
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
  }

  public companion object {
    public fun actionRotasMotoristaFragmentToDetalhesEntregaFragment(dataSelecionada: Long,
        rota: Rota?): NavDirections =
        ActionRotasMotoristaFragmentToDetalhesEntregaFragment(dataSelecionada, rota)
  }
}
