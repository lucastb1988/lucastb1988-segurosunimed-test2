package com.example.api.feign.cepvalidador;

import com.example.api.feign.cepvalidador.response.ZipCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "cepvalidador", url = "viacep.com.br/ws")
public interface ValidateCepFeignRepository {

    @GetMapping(path = "/{cep}/json", produces = "application/json",
            consumes = "application/json")
    ZipCodeResponse validateZipCode(@PathVariable String cep);
}
