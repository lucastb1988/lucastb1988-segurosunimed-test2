package com.example.api.service;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.example.api.domain.security.ProfileEnum;
import com.example.api.domain.security.User;
import com.example.api.repository.AddressRepository;
import com.example.api.repository.CustomerRepository;
import com.example.api.repository.security.UserRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DBService {
    
    private CustomerRepository customerRepository;

    private AddressRepository addressRepository;

    private UserRepository userRepository;

    @Autowired
    public DBService(CustomerRepository customerRepository, AddressRepository addressRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public void instanciarDataBase() {

        Customer c1 = new Customer(null, "Lucas Tartarini", "lucas@teste.com.br", "M");
        Address ad1 = new Address(null, "Rua Bandeirante 156", "Casa", "Barcelona", "São Caetano do Sul", "SP", "09570010");
        Address ad2 = new Address(null, "Rua Nazareth 693", "Sobrado", "Barcelona", "São Caetano do Sul", "SP", "09550001");

        Customer c2 = new Customer(null, "Renata Tartarini", "renata@teste.com.br", "F");
        Address ad3 = new Address(null, "Rua Conselheiro Lafayetter 793", "Apartamento", "Barcelona", "São Caetano do Sul", "SP", "09580020");
        Address ad4 = new Address(null, "Rua Monaco 789", "Sem complemento", "Olimpo", "São Caetano do Sul", "SP", "09566030");

        c1.getAddresses().addAll(Arrays.asList(ad1, ad2));
        ad1.setCustomer(c1);
        ad2.setCustomer(c1);

        c2.getAddresses().addAll(Arrays.asList(ad3, ad4));
        ad3.setCustomer(c2);
        ad4.setCustomer(c2);

        customerRepository.saveAll(Arrays.asList(c1, c2));
        addressRepository.saveAll(Arrays.asList(ad1, ad2, ad3, ad4));

        User user1 = new User(null, "Lucas Tartarini", "lucas.t.banin@gmail.com", "abc123", Lists.newArrayList(ProfileEnum.ADMIN.getDescription(), ProfileEnum.FUNCIONARIO.getDescription()));
        User user2 = new User(null, "Gustavo Tartarini", "gustavo.t.banin@gmail.com", "abc123", Lists.newArrayList(ProfileEnum.FUNCIONARIO.getDescription()));
        userRepository.saveAll(Arrays.asList(user1, user2));
    }
}