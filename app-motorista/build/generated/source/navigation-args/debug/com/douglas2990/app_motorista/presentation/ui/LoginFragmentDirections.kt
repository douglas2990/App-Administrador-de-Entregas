package com.douglas2990.app_motorista.presentation.ui

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.douglas2990.app_motorista.R

public class LoginFragmentDirections private constructor() {
  public companion object {
    public fun actionLoginFragmentToMinhaAgendaFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_loginFragment_to_minhaAgendaFragment)
  }
}
