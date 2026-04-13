package com.douglas2990.app_motorista.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.douglas2990.app_motorista.R
import com.douglas2990.app_motorista.data.repository.model.AgendaDia
import com.douglas2990.app_motorista.databinding.FragmentRotasMotoristaBinding
import com.example.core.model.Rota
import com.douglas2990.app_motorista.presentation.ui.adapter.RotaAdapter
import com.douglas2990.app_motorista.presentation.viewmodel.RotasMotoristaViewModel
import com.example.core.UIstatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.core.model.Motorista
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class RotasMotoristaFragment : Fragment() {

    private var _binding: FragmentRotasMotoristaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RotasMotoristaViewModel by viewModels()

    //val dataSelecionada = arguments?.getString("data") ?: ""
    private var nomeEmpresaAtual: String = "D2990 Entregas"




    private lateinit var rotaAdapter: RotaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRotasMotoristaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupRecyclerView()
        setupObservers()


        // Pegamos o objeto AgendaDia que você enviou da Agenda
        val agendaDia = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13 ou superior
            arguments?.getParcelable("agenda_dia", AgendaDia::class.java)
        } else {
            // Para versões anteriores (o cast manual ainda é necessário aqui)
            @Suppress("DEPRECATION")
            arguments?.getParcelable<AgendaDia>("agenda_dia")
        }

        val dataParaFiltrar = agendaDia?.data ?: ""
        Log.d("DEBUG_ROTAS", "Data extraída: $dataParaFiltrar")

        // Dispara a busca das rotas vinculadas ao UID do motorista logado
        // Agora sim, com a data preenchida, chamamos o ViewModel
        if (dataParaFiltrar.isNotEmpty()) {
            viewModel.observarMinhasRotas(dataParaFiltrar)
            viewModel.carregarDadosMotorista()
        } else {
            Log.e("DEBUG_ROTAS", "ERRO: dataParaFiltrar continua vazia!")
            binding.textListaVazia.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        rotaAdapter = RotaAdapter(
            onItemClick = { rota ->
                irParaTelaEntrega(rota)
            },
            onItemLongClick = { /* O motorista não exclui rotas, apenas visualiza */ }
        )

        binding.rvRotasMotorista.apply {
            adapter = rotaAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {

        viewModel.nomeEmpresa.observe(viewLifecycleOwner) { empresa ->
            if (!empresa.isNullOrBlank()) {
                nomeEmpresaAtual = empresa
            }
        }

        viewModel.rotas.observe(viewLifecycleOwner) { status ->

            // REGRA DE OURO: Se não está mais carregando, para as animações
            if (status !is UIstatus.Carregando) {
                binding.progressBar.visibility = View.GONE
                //binding.swipeRefresh.isRefreshing = false
            }

            when (status) {
                is UIstatus.Carregando -> {
                    // Só mostra o progress central se não for um "puxar para atualizar"
                   // if (!binding.swipeRefresh.isRefreshing) {
                        binding.progressBar.visibility = View.VISIBLE
                    binding.textListaVazia.visibility = View.GONE
                    //}
                }
                is UIstatus.Sucesso -> {
                    val lista = status.dados ?: emptyList()
                    rotaAdapter.submitList(lista)

                    val todasConcluidas = lista.isNotEmpty() && lista.all { it.status != "PENDENTE" }

                    binding.btnEnviarRelatorio.visibility = if (todasConcluidas) View.VISIBLE else View.GONE



                    btnEnviarRelatorioPDF(lista, nomeEmpresaAtual)

                    // Gerencia o texto de lista vazia
                    binding.textListaVazia.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
                }
                is UIstatus.Erro -> {
                    // Usa a variável 'mensagem' que definimos no :core
                    Toast.makeText(requireContext(), status.erro, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irParaTelaEntrega(rota: Rota) {
        // Usando Bundle para passar a rota selecionada para o DetalhesEntregaFragment
        val bundle = Bundle().apply {
            putParcelable("rota", rota)
        }
        findNavController().navigate(
            R.id.action_rotasMotoristaFragment_to_detalhesEntregaFragment,
            bundle
        )
    }

    private fun gerarRelatorioTexto(lista: List<Rota>): String {
        val dataHoje = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val sb = StringBuilder()

        sb.append("📦 *RELATÓRIO DE ENTREGAS - $dataHoje*\n")
        sb.append("──────────────────────\n\n")

        lista.forEachIndexed { index, rota ->
            sb.append("*${index + 1}. CLIENTE:* ${rota.nomeEmpresaDestino.uppercase()}\n")
            sb.append("🏠 *ENDEREÇO:* ${rota.endereco}\n")
            sb.append("🏁 *STATUS:* ✅ ${rota.status}\n")

            if (!rota.comprovanteUrl.isNullOrBlank()) {
                sb.append("🔗 *COMPROVANTE:* ${rota.comprovanteUrl}\n")
            }
            sb.append("──────────────────────\n")
        }

        sb.append("\n_Relatório gerado automaticamente pelo App Motorista_")
        return sb.toString()
    }

    fun botaoEnviarWhatZap(){
        binding.btnEnviarRelatorio.setOnClickListener {
            val listaConcluida = rotaAdapter.currentList.filter { it.status != "PENDENTE" }

            if (listaConcluida.isNotEmpty()) {
                val mensagemFormatada = gerarRelatorioTexto(listaConcluida)
                val numeroWhats = "5511993825047" // Já com o 55 do Brasil

                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    // Monta a URL do WhatsApp: https://wa.me/numero?text=mensagem
                    val url = "https://api.whatsapp.com/send?phone=$numeroWhats&text=${java.net.URLEncoder.encode(mensagemFormatada, "UTF-8")}"
                    intent.data = android.net.Uri.parse(url)
                    startActivity(intent)

                    // LOGICA DE HISTÓRICO:
                    // Após abrir o Whats, você pode disparar a função que move para o histórico
                    // viewModel.moverParaHistorico(listaConcluida)

                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "WhatsApp não instalado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun gerarRelatorioPDF(lista: List<Rota>) {
        val pdfDocument = android.graphics.pdf.PdfDocument()
        val paint = android.graphics.Paint()
        val titlePaint = android.graphics.Paint()

        // Configuração da Página A4
        val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Cabeçalho
        titlePaint.textSize = 18f
        titlePaint.isFakeBoldText = true
        canvas.drawText("RELATÓRIO DE ENTREGAS CONCLUÍDAS", 40f, 50f, titlePaint)

        paint.textSize = 12f
        val dataHoje = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
        canvas.drawText("Data de Emissão: $dataHoje", 40f, 80f, paint)
        canvas.drawLine(40f, 90f, 555f, 90f, paint)

        var y = 120f

        lista.forEach { rota ->
            // Se o PDF ficar muito longo, você precisaria criar novas páginas (lógica de paginação)
            paint.isFakeBoldText = true
            canvas.drawText("CLIENTE: ${rota.nomeEmpresaDestino.uppercase()}", 40f, y, paint)
            y += 20f

            paint.isFakeBoldText = false
            canvas.drawText("Endereço: ${rota.endereco}", 40f, y, paint)
            y += 15f
            canvas.drawText("Status: ${rota.status}", 40f, y, paint)
            y += 15f

            if (!rota.comprovanteUrl.isNullOrEmpty()) {
                canvas.drawText("Link do Comprovante: Ver Foto no App/Firebase", 40f, y, paint)
                y += 15f
            }

            canvas.drawLine(40f, y, 555f, y, paint)
            y += 30f

            // Proteção para não sair da página (simplificado)
            if (y > 750f) return@forEach
        }

        pdfDocument.finishPage(page)

        // Salva no Cache
        val file = java.io.File(requireContext().cacheDir, "Relatorio_Entregas.pdf")
        try {
            pdfDocument.writeTo(java.io.FileOutputStream(file))
            enviarDocumento(file)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }
    }

    private fun enviarArquivo(file: java.io.File) {
        val uri = androidx.core.content.FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // Se quiser fixar o WhatsApp:
        // intent.setPackage("com.whatsapp")

        startActivity(Intent.createChooser(intent, "Compartilhar Relatório"))
    }

    private fun enviarDocumento(file: java.io.File) {
        val uri = androidx.core.content.FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Comprovantes de Entrega")
            putExtra(Intent.EXTRA_TEXT, "Olá, segue em anexo o relatório de entregas concluídas.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(Intent.createChooser(intent, "Enviar Relatório Profissional"))
    }

    fun btnEnviarRelatorioPDF(lista: List<Rota>, nomeEmpresaFallback: String){
        binding.btnEnviarRelatorio.setOnClickListener {
            //gerarRelatorioPDF(lista)
            val empresaFinal = viewModel.nomeEmpresa.value ?: nomeEmpresaFallback
            gerarRelatorioPDFComFotos(lista,empresaFinal)
        }
    }

    private suspend fun baixarBitmap(url: String): android.graphics.Bitmap? {

        return withContext(Dispatchers.IO) { // Apenas Dispatchers.IO entre parênteses
            try {
                Glide.with(requireContext())
                    .asBitmap()
                    .load(url)
                    .submit(200, 200)
                    .get()
            } catch (e: Exception) {
                null
            }
        }
    }

    // Adicione o parâmetro nomeEmpresa para ser dinâmico
    private fun gerarRelatorioPDFComFotos(lista: List<Rota>, nomeEmpresaFirebase: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (lista.isEmpty()) return@launch

            val pdfDocument = android.graphics.pdf.PdfDocument()
            val paint = android.graphics.Paint()
            val titlePaint = android.graphics.Paint()
            val headerPaint = android.graphics.Paint()

            val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
            var page = pdfDocument.startPage(pageInfo)
            var canvas = page.canvas

            var y = 50f
            val primeiraRota = lista[0]

            // --- 1. CABEÇALHO ---
            titlePaint.textSize = 22f
            titlePaint.isFakeBoldText = true
            titlePaint.color = android.graphics.Color.rgb(44, 62, 80)
            canvas.drawText("RELATÓRIO DE ENTREGAS", 40f, y, titlePaint)
            y += 35f

            headerPaint.textSize = 12f
            headerPaint.isAntiAlias = true
            headerPaint.color = android.graphics.Color.BLACK

            canvas.drawText("Motorista: ${primeiraRota.nomeMotorista}", 40f, y, headerPaint)
            y += 20f

            // AQUI: Usando o nome que o ViewModel buscou no Firebase
            headerPaint.isFakeBoldText = true
            canvas.drawText("Empresa: ${nomeEmpresaFirebase.uppercase()}", 40f, y, headerPaint)
            headerPaint.isFakeBoldText = false
            y += 20f

            val dataHoje = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            canvas.drawText("Emissão: $dataHoje", 40f, y, headerPaint)
            y += 25f

            paint.strokeWidth = 2f
            paint.color = android.graphics.Color.LTGRAY
            canvas.drawLine(40f, y, 555f, y, paint)
            y += 40f

            paint.color = android.graphics.Color.BLACK

            // --- 2. LISTAGEM ---
            lista.forEachIndexed { index, rota ->
                if (y > 700f) {
                    pdfDocument.finishPage(page)
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    y = 50f
                }

                paint.isFakeBoldText = true
                paint.textSize = 14f
                val idExibicao = rota.os.ifBlank { rota.id.takeLast(8) }
                canvas.drawText("O.S.: #$idExibicao", 40f, y, paint)
                y += 22f

                paint.textSize = 12f
                canvas.drawText("${index + 1}. CLIENTE: ${rota.nomeEmpresaDestino.uppercase()}", 40f, y, paint)
                y += 18f

                paint.isFakeBoldText = false
                canvas.drawText("Endereço: ${rota.endereco}", 40f, y, paint)
                y += 20f

                if (!rota.comprovanteUrl.isNullOrEmpty()) {
                    val bitmap = baixarBitmap(rota.comprovanteUrl.toString())
                    if (bitmap != null) {
                        canvas.drawBitmap(bitmap, 40f, y, paint)
                        y += 130f
                    }
                } else {
                    paint.color = android.graphics.Color.RED
                    canvas.drawText("[ENTREGA SEM FOTO DE COMPROVANTE]", 40f, y, paint)
                    paint.color = android.graphics.Color.BLACK
                    y += 20f
                }

                y += 10f
                paint.strokeWidth = 1f
                paint.color = android.graphics.Color.LTGRAY
                canvas.drawLine(40f, y, 555f, y, paint)
                y += 35f
                paint.color = android.graphics.Color.BLACK
            }

            pdfDocument.finishPage(page)

            // CORREÇÃO: Usando requireContext().cacheDir diretamente para evitar erros de referência
            val nomeArquivo = "Relatorio_${primeiraRota.nomeMotorista.filter { it.isLetterOrDigit() }}_${System.currentTimeMillis()}.pdf"
            val file = java.io.File(requireContext().cacheDir, nomeArquivo)

            try {
                pdfDocument.writeTo(java.io.FileOutputStream(file))
                enviarDocumento(file)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pdfDocument.close()
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}