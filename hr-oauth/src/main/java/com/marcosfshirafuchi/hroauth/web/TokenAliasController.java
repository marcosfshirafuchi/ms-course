package com.marcosfshirafuchi.hroauth.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class TokenAliasController {

    // Alias para compatibilizar com o caminho antigo/esperado pelo gateway
    @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String forwardToSasDefault() {
        // Encaminha a mesma requisição (mantém Basic Auth e o form-url-encoded)
        return "forward:/oauth2/token";
    }
}
