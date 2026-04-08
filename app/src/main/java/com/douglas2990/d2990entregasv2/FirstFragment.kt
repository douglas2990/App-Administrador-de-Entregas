package com.douglas2990.d2990entregasv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.douglas2990.d2990entregasv2.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.buttonCadastrarEmpresaFragment.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_cadastrarEmpresaFragment)
        }

        binding.buttonCadastrarMotoristaFragment.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_CadastrarMotoristaFragment)
        }
        binding.buttonMostrarMotoristaCadastradoFragment.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_motoristasCadastradosFragment)
        }
        binding.buttonCadastrarLoginMotorista.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_cadastroAcessoFragment)
        }
        binding.buttonCadastrarRotaMotorista.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_criarRotaProMotoristaFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}