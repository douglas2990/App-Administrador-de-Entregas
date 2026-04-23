package com.douglas2990.d2990entregasv2.presentation.viewmodel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.UploadRepository
import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.user.IAutenticacaoRepository
import com.douglas2990.d2990entregasv2.domain.usecase.AutenticacaoUseCase
import com.douglas2990.d2990entregasv2.domain.usecase.ResultadoValidacao
import com.douglas2990.d2990entregasv2.model.user.UploadStorage
import com.douglas2990.d2990entregasv2.model.user.Usuario
import com.example.core.UIstatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AutenticacaoViewModel @Inject constructor(
    private val autenticacaoUseCase: AutenticacaoUseCase,
    private val autenticacaoRepositoryImpl: IAutenticacaoRepository,
    private val uploadRepository: UploadRepository
): ViewModel() {

    private val _statusCadastro = MutableLiveData<UIstatus<Boolean>>()
    val statusCadastro: LiveData<UIstatus<Boolean>> = _statusCadastro

    private val _resultadoValidacao = MutableLiveData<ResultadoValidacao>()
    val resultadoValidacao: LiveData<ResultadoValidacao>
        get() = _resultadoValidacao

    private val _carregando = MutableLiveData<Boolean>()
    val carregando: LiveData<Boolean>
        get() = _carregando

    fun cadastrarUsuario(usuario: Usuario) {
        val retornoValidacao = autenticacaoUseCase.validarCadastroUsuario(usuario)
        _resultadoValidacao.value = retornoValidacao

        if (retornoValidacao.sucessoValidacaoCadastro) {
            _carregando.value = true
            viewModelScope.launch {
                autenticacaoRepositoryImpl.cadastrarUsuario(usuario) { status ->
                    _statusCadastro.postValue(status)
                    _carregando.postValue(false)
                }
            }
        } else {
            // MUITO IMPORTANTE: Se a validação falhar, pare o carregamento
            // e avise a UI para que o usuário saiba o que houve
            _carregando.value = false
            _statusCadastro.value = UIstatus.Erro("Verifique os campos em vermelho.")
        }
    }

    fun logarUsuario(usuario: Usuario, uiStatus: (UIstatus<Boolean>)->Unit ){
        val retornoValidacao = autenticacaoUseCase.validarLoginUsuario( usuario )
        _resultadoValidacao.value = retornoValidacao
        if( retornoValidacao.sucessoValidacaoLogin ){
            _carregando.value = true
            viewModelScope.launch {
                autenticacaoRepositoryImpl.logarUsuario( usuario, uiStatus )
                _carregando.postValue( false )
            }
        }
    }

    fun verificarUsuarioLogado() : Boolean {
        return autenticacaoRepositoryImpl.verificarUsuarioLogado()
    }

    fun recuperarIdUsuarioLogado() : String {
        return autenticacaoRepositoryImpl.recuperarIdUsuarioLogado()
    }

    fun deslogarUsuario() {
        return autenticacaoRepositoryImpl.deslogarUsuario()
    }

    fun recuperarDadosUsuarioLogado(
        uiStatus: (UIstatus<Usuario>)->Unit
    ){
        uiStatus.invoke(UIstatus.Carregando)
        viewModelScope.launch {
            autenticacaoRepositoryImpl.recuperarDadosUsuarioLogado( uiStatus )
        }
    }

    fun atualizarUsuario(
        usuario: Usuario,
        uiStatus: (UIstatus<String>)->Unit
    ){
        uiStatus.invoke(UIstatus.Carregando)
        viewModelScope.launch {
            autenticacaoRepositoryImpl.atualizarUsuario( usuario, uiStatus )
        }
    }

    fun uploadImagem(
        uploadStorage: UploadStorage,
        uiStatus: (UIstatus<String>)->Unit
    ){
        uiStatus.invoke( UIstatus.Carregando )
        viewModelScope.launch {
            val upload = async {
                uploadRepository.upload(
                    uploadStorage.local,
                    uploadStorage.nomeImagem,
                    uploadStorage.uriImagem
                )
            }
            val uiStatusUpload = upload.await()
            if( uiStatusUpload is UIstatus.Sucesso ){

                val urlImagem = uiStatusUpload.dados
                val usuario = Usuario(urlPerfil = urlImagem)
                autenticacaoRepositoryImpl.atualizarUsuario( usuario, uiStatus )
                uiStatus.invoke( UIstatus.Sucesso("") )

            }else{
                uiStatus.invoke( UIstatus.Erro("Erro ao fazer Upload") )
            }
        }

    }

}