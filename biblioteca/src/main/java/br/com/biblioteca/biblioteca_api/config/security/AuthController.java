package br.com.biblioteca.biblioteca_api.config.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Autenticação", description = "Endpoint para obter o token de autenticação.")
public class AuthController {

    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário e retorna um token JWT",
            description = "Use as credenciais de um usuário existente (ex: admin@email.com / 123) para obter um token no cabeçalho 'Authorization' da resposta.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais do usuário para login.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CredenciaisDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida. O token JWT é retornado no cabeçalho 'Authorization'.",
                            headers = @io.swagger.v3.oas.annotations.headers.Header(
                                    name = "Authorization",
                                    description = "Bearer token para autenticação",
                                    schema = @Schema(type = "string")
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas.", content = @Content)
            })
    public void fakeLogin(@RequestBody CredenciaisDTO credenciaisDTO) {
        throw new UnsupportedOperationException("Este endpoint é gerenciado pelo Spring Security e não deve ser chamado diretamente.");
    }
}
