package com.douglas2990.d2990entregasv2.domain.usecase

import com.douglas2990.d2990entregasv2.data.remote.firebase.repository.IMotoristaRepository
import com.douglas2990.d2990entregasv2.model.Motorista
import com.example.core.UIstatus
import com.example.core.util.ValidadorEmail
import javax.inject.Inject

class SalvarMotoristaUseCase @Inject constructor(
    private val repository: IMotoristaRepository
) {
    suspend operator fun invoke(motorista: Motorista): UIstatus<String> {

        // 1. Validações de Campos Obrigatórios
        if (motorista.nome.isBlank()) return UIstatus.Erro("O nome é obrigatório")
        if (motorista.telefone.length < 10) return UIstatus.Erro("Telefone inválido")

        // 2. Usando seu Validador de E-mail do :core
        if (!ValidadorEmail.isValido(motorista.email)) {
            return UIstatus.Erro("E-mail com formato inválido")
        }

        // 3. Validação da Senha (Regra de negócio: mínimo 6 caracteres)
        if (motorista.senha.length < 6) {
            return UIstatus.Erro("A senha inicial deve ter pelo menos 6 caracteres")
        }

        // 4. Validação da Empresa (O que vem do Spinner)
        if (motorista.idEmpresa.isEmpty()) {
            return UIstatus.Erro("Selecione uma empresa para este motorista")
        }

        // 5. Chamada ao Repository (CORREÇÃO AQUI)
        // Passamos o objeto e também extraímos o e-mail e a senha
        // para a criação da conta no Firebase Auth
        return repository.salvar(
            motorista = motorista,
            email = motorista.email,
            senha = motorista.senha
        )
    }
}