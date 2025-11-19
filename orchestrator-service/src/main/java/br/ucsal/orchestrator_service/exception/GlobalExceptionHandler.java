package br.ucsal.orchestrator_service.exception; // <- ATUALIZADO

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Captura exceções de todos os controllers
public class GlobalExceptionHandler {

    // Captura qualquer erro vindo de um Feign Client
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex) {

        // Se o erro for 404 (Not Found)
        if (ex.status() == HttpStatus.NOT_FOUND.value()) {
            // Você pode até ler a mensagem de erro que o outro serviço enviou
            // String errorMessage = ex.contentUTF8();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Retorna 400 para o usuário final
                    .body("Erro de validação: " + ex.getMessage()); // Mensagem simples
        }

        // Se o erro for 401 (Token inválido)
        if (ex.status() == HttpStatus.UNAUTHORIZED.value()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Token de autenticação inválido ou expirado.");
        }

        // Erro genérico de servidor
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno ao comunicar com outros serviços.");
    }
}