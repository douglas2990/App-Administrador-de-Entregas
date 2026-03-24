package com.douglas2990.app_motorista.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.douglas2990.app_motorista.databinding.FragmentDetalhesEntregaBinding
import com.douglas2990.app_motorista.presentation.viewmodel.DetalhesEntregaViewModel
import com.example.core.model.Rota
import com.example.core.UIstatus
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DetalhesEntregaFragment : Fragment() {

    private var _binding: FragmentDetalhesEntregaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetalhesEntregaViewModel by viewModels()
    private var rota: Rota? = null
    private var imageUri: Uri? = null

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            binding.imgComprovante.setImageURI(imageUri)
            binding.btnFinalizarEntrega.visibility = View.VISIBLE
        } else {
            Toast.makeText(context, "Falha ao capturar imagem", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            rota = it.getParcelable("rota")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalhesEntregaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
        setupObservers()
    }

    private fun setupUI() {
        rota?.let {
            binding.textOS.text = "OS: ${it.os}"
            binding.textEmpresaDestino.text = it.nomeEmpresaDestino
            binding.textEndereco.text = it.endereco
        }
    }

    private fun setupListeners() {
        binding.btnTirarFoto.setOnClickListener {
            prepararCamera()
        }

        binding.btnNavegar.setOnClickListener {
            abrirMapa()
        }

        binding.btnFinalizarEntrega.setOnClickListener {
            rota?.let { r ->
                imageUri?.let { uri ->
                    viewModel.finalizarEntrega(r.id, uri)
                }
            }
        }

        binding.btnReportarProblema.setOnClickListener {
            Toast.makeText(context, "Funcionalidade de problema em breve", Toast.LENGTH_SHORT).show()
        }
    }

    private fun abrirMapa() {
        rota?.let {
            val uriEndereco = Uri.encode(it.endereco)
            val mapIntentUri = Uri.parse("google.navigation:q=$uriEndereco")
            val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            
            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(mapIntent)
            } else {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=$uriEndereco"))
                startActivity(webIntent)
            }
        }
    }

    private fun setupObservers() {
        viewModel.statusEntrega.observe(viewLifecycleOwner) { status ->
            when (status) {
                is UIstatus.Carregando -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnFinalizarEntrega.isEnabled = false
                }
                is UIstatus.Sucesso -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Entrega finalizada com sucesso!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnFinalizarEntrega.isEnabled = true
                    Toast.makeText(context, status.erro, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun prepararCamera() {
        val photoFile = File.createTempFile(
            "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}_",
            ".jpg",
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            photoFile
        )

        takePictureLauncher.launch(imageUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
