package com.example.api.web.rest;

import com.example.api.service.security.AuthenticationService;
import com.example.api.web.rest.request.AuthenticationRequest;
import com.example.api.web.rest.response.AuthenticationResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"API dedicada a autenticação do login."})
@RestController
@RequestMapping("/authenticate")
public class AutenticacaoController {

    private AuthenticationService authenticationService;

    @Autowired
    public AutenticacaoController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation("Autenticação de usuário")
    @PostMapping(value = "/login")
    @ResponseStatus(value = HttpStatus.OK)
    public AuthenticationResponse login(
            @RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.login(authenticationRequest.getUser());
    }
}