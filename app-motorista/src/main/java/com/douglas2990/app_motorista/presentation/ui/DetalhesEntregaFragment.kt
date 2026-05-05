package com.douglas2990.app_motorista.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.douglas2990.app_motorista.R
import com.douglas2990.app_motorista.databinding.DialogReportarProblemaBinding
import com.douglas2990.app_motorista.databinding.FragmentDetalhesEntregaBinding
import com.douglas2990.app_motorista.presentation.viewmodel.DetalhesEntregaViewModel
import com.example.core.model.Rota
import com.example.core.UIstatus
import com.example.core.util.ShareHelper
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

    // Variável para saber se estamos tirando foto de Sucesso ou de Problema
    private var motivoAtual: String? = null

    private var telefoneDoGestor: String? = null
    private val shareHelper by lazy { ShareHelper(requireContext()) }

    private var ehAUltimaEntrega: Boolean = false

    // 1. Launcher para pedir Permissão da Câmera
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            prepararCamera()
        } else {
            Toast.makeText(context, "Permissão da câmera necessária para fotos", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            binding.imgComprovante.setImageURI(imageUri)
            binding.imgComprovante.visibility = View.VISIBLE

            // Se tiver um motivo, é um problema com foto. Se não, é entrega normal.
            if (motivoAtual != null) {
                enviarRelatoProblema(motivoAtual!!)
            } else {
                binding.btnFinalizarEntrega.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            rota = it.getParcelable("rota")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetalhesEntregaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {
            rota = it.getParcelable("rota")
            // Captura se esta entrega era a última da lista
            ehAUltimaEntrega = it.getBoolean("sou_a_ultima", false)
        }


        setupUI()
        setupListeners()
        setupObservers()
        rota?.let {
            viewModel.buscarTelefoneAdmin(it.idGestor)
        }
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
            motivoAtual = null // Resetando para garantir que é fluxo de sucesso
            verificarPermissaoECamera()
        }

        binding.btnFinalizarEntrega.setOnClickListener {
            rota?.let { r ->
                imageUri?.let { uri ->
                    viewModel.finalizarEntrega(r.id, uri)
                }
            }
        }

        binding.btnReportarProblema.setOnClickListener {
            exibirDialogProblema()
        }

        binding.btnNavegar.setOnClickListener { abrirMapa() }
    }

    // 2. Função de Segurança para Permissão
    private fun verificarPermissaoECamera() {
        when {
            androidx.core.content.ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED -> {
                prepararCamera()
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun prepararCamera() {
        try {
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
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao criar arquivo de imagem", Toast.LENGTH_SHORT).show()
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
                    val telefoneDestino = viewModel.telefoneAdmin.value ?: ""
                    Toast.makeText(context, status.dados, Toast.LENGTH_SHORT).show()


                    if (ehAUltimaEntrega) {
                        val result = Bundle().apply {
                            putBoolean("ultima_finalizada", true)
                        }
                        parentFragmentManager.setFragmentResult("chave_finalizacao", result)
                    }



                    findNavController().popBackStack()
                }
                is UIstatus.Erro -> {
                    binding.progressBar.visibility = View.GONE
                    binding.btnFinalizarEntrega.isEnabled = true
                    Toast.makeText(context, status.erro, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.telefoneAdmin.observe(viewLifecycleOwner) { telefone ->
            if (telefone != null) {
                this.telefoneDoGestor = telefone
                Log.d("DETALHES", "Telefone do Admin pronto: $telefone")
            }
        }

    }

    private fun exibirDialogProblema() {
        val dialogBinding = DialogReportarProblemaBinding.inflate(layoutInflater)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.radioGroupProblemas.setOnCheckedChangeListener { _, checkedId ->
            dialogBinding.inputLayoutOutro.visibility =
                if (checkedId == R.id.radioOutro) View.VISIBLE else View.GONE
        }

        dialogBinding.btnConfirmarProblema.setOnClickListener {
            val motivoId = dialogBinding.radioGroupProblemas.checkedRadioButtonId
            if (motivoId == -1) {
                Toast.makeText(context, "Selecione um motivo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val motivoTexto = when (motivoId) {
                R.id.radioRecusado -> "Recusado pelo cliente"
                R.id.radioHorarioEncerrado -> "Horário encerrado"
                R.id.radioNaoDeuTempo -> "Não deu tempo"
                R.id.radioSemComprovante -> "Sem comprovante do cliente"
                R.id.radioOutro -> dialogBinding.editOutroMotivo.text.toString()
                else -> ""
            }

            if (motivoId == R.id.radioOutro && motivoTexto.isEmpty()) {
                dialogBinding.editOutroMotivo.error = "Descreva o motivo"
                return@setOnClickListener
            }

            dialog.dismiss()
            confirmarComFotoOpcional(motivoTexto)
        }
        dialog.show()
    }

    private fun confirmarComFotoOpcional(motivo: String) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Deseja tirar foto?")
            .setMessage("Deseja tirar uma foto para comprovar a situação?")
            .setPositiveButton("Sim, tirar foto") { _, _ ->
                motivoAtual = motivo
                verificarPermissaoECamera()
            }
            .setNegativeButton("Não, apenas enviar") { _, _ ->
                enviarRelatoProblema(motivo)
            }
            .show()
    }

    private fun enviarRelatoProblema(motivo: String) {
        rota?.let { r ->
            // IMPORTANTE: Chamando a função correta do ViewModel
            viewModel.reportarProblema(r.id, motivo, imageUri)
        }
    }

    private fun abrirMapa() {
        rota?.let { r ->
            val endereco = r.endereco
            if (endereco.isNotEmpty()) {
                // Criamos a URI de busca. O "q=" faz o mapa procurar o endereço exato.
                val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(endereco)}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

                // Verifica se existe algum app de mapa instalado
                if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    Toast.makeText(context, "Nenhum aplicativo de mapas encontrado", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Endereço não disponível para esta rota", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}