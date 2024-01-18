package com.example.api.web.rest;

import com.example.api.domain.Customer;
import com.example.api.domain.security.ProfileEnum;
import com.example.api.service.CustomerService;
import com.example.api.service.security.AuthorizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api(tags = {"API dedicada as operações dos clientes."})
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;

    private AuthorizationService authorizationService;

    @Autowired
    public CustomerController(CustomerService service, AuthorizationService authorizationService) {
        this.customerService = service;
        this.authorizationService = authorizationService;
    }

    @ApiOperation("Salvar cliente.")
    @PostMapping
    public ResponseEntity<Void> create(@RequestHeader String token, @Valid @RequestBody Customer obj) {
        authorize(token, ProfileEnum.ADMIN);
        obj = customerService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation("Atualizar cliente.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestHeader String token, @Valid @RequestBody Customer obj, @PathVariable Long id) {
        authorize(token, ProfileEnum.ADMIN, ProfileEnum.FUNCIONARIO);
        customerService.update(obj, id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Deletar cliente.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader String token, @PathVariable Long id) {
        authorize(token, ProfileEnum.ADMIN);
        customerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("Buscar todos os clientes inseridos no banco.")
    @GetMapping
    public ResponseEntity<List<Customer>> findAll(@RequestHeader String token) {
        authorize(token, ProfileEnum.ADMIN, ProfileEnum.FUNCIONARIO);
        return ResponseEntity.ok().body(customerService.findAll());
    }

    @ApiOperation("Buscar os clientes de acordo com o filtro de nome, email e genero.")
    @GetMapping("/filtered")
    public ResponseEntity<List<Customer>> findByFilters(@RequestHeader String token,
                                                        @RequestParam(value = "name", required = false) final String name,
                                                        @RequestParam(value = "email", required = false) final String email,
                                                        @RequestParam(value = "gender", required = false) final String gender) {
        authorize(token, ProfileEnum.ADMIN, ProfileEnum.FUNCIONARIO);
        return ResponseEntity.ok().body(customerService.findByFilters(name, email, gender));
    }

    @ApiOperation("Buscar os clientes inseridos no banco de forma paginada.")
    @GetMapping("/paginated")
    public ResponseEntity<Page<Customer>> findPaginated(
            @RequestHeader String token,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "qtyPerPage", defaultValue = "10") Integer qtyPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        authorize(token, ProfileEnum.ADMIN, ProfileEnum.FUNCIONARIO);
        return ResponseEntity.ok().body(customerService.findPaginated(page, qtyPerPage, orderBy, direction));
    }

    @ApiOperation("Buscar cliente inserido no banco de acordo com seu id.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Customer findById(@RequestHeader String token, @PathVariable Long id) {
        authorize(token, ProfileEnum.ADMIN, ProfileEnum.FUNCIONARIO);
        return customerService.findById(id).get();
    }

    @ApiOperation("Buscar cliente por nome.")
    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public Customer findByName(@RequestHeader String token, @PathVariable String name) {
        authorize(token, ProfileEnum.ADMIN, ProfileEnum.FUNCIONARIO);
        return customerService.findByName(name);
    }

    @ApiOperation("Buscar cliente por email.")
    @GetMapping("/email/{email}")
    @ResponseStatus(HttpStatus.OK)
    public Customer findByEmail(@RequestHeader String token, @PathVariable String email) {
        authorize(token, ProfileEnum.ADMIN, ProfileEnum.FUNCIONARIO);
        return customerService.findByEmail(email);
    }

    @ApiOperation("Buscar cliente por genero.")
    @GetMapping("/gender/{gender}")
    @ResponseStatus(HttpStatus.OK)
    public List<Customer> findByGender(@RequestHeader String token, @PathVariable String gender) {
        authorize(token, ProfileEnum.ADMIN, ProfileEnum.FUNCIONARIO);
        return customerService.findByGender(gender);
    }

    private void authorize(String token, ProfileEnum... profiles) {
        try {
            authorizationService.authorize(token, profiles);
        } catch (Exception e) {
            throw e;
        }
    }
}
