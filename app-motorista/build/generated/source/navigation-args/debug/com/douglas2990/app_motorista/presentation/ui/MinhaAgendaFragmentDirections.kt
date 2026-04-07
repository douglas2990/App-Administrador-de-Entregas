package com.douglas2990.app_motorista.presentation.ui

import android.os.Bundle
import androidx.navigation.NavDirections
import com.douglas2990.app_motorista.R
import kotlin.Int
import kotlin.Long

public class MinhaAgendaFragmentDirections private constructor() {
  private data class ActionMinhaAgendaToRotasMotoristaFragment(
    public val dataSelecionada: Long,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_minhaAgenda_to_rotasMotoristaFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putLong("dataSelecionada", this.dataSelecionada)
        return result
      }
  }

  public companion object {
    public fun actionMinhaAgendaToRotasMotoristaFragment(dataSelecionada: Long): NavDirections =
        ActionMinhaAgendaToRotasMotoristaFragment(dataSelecionada)
  }
}
