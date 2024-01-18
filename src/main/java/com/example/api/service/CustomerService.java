package com.example.api.service;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.example.api.exception.ObjectNotFoundException;
import com.example.api.feign.cepvalidador.ValidateCepFeignRepository;
import com.example.api.feign.cepvalidador.response.ZipCodeResponse;
import com.example.api.repository.AddressRepository;
import com.example.api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    private AddressRepository addressRepository;

    private ValidateCepFeignRepository validateCepFeignRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository, ValidateCepFeignRepository validateCepFeignRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.validateCepFeignRepository = validateCepFeignRepository;
    }

    public Customer create(Customer obj) {
        obj.setId(null);
        if (!obj.getAddresses().isEmpty()) {
            for (Address address : obj.getAddresses()) {
                if (address.getZipCode().length() != 8) {
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "ZipCode provided must have 8 characters!");
                }

                ZipCodeResponse response = validateCepFeignRepository.validateZipCode(address.getZipCode());
                if (response == null || response.getCep() == null) {
                    throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "ZipCode provided do not exist!!");
                }
                address.setStreet(response.getLogradouro());
                address.setComplement(response.getComplemento());
                address.setNeighborhood(response.getBairro());
                address.setCity(response.getLocalidade());
                address.setState(response.getUf());
                address.setCustomer(obj);
            }
        }

        obj = customerRepository.save(obj);
        addressRepository.saveAll(obj.getAddresses());
        return obj;
    }

    public void update(Customer obj, Long id) {
        Optional<Customer> newObj = findById(id);
        if (!newObj.isPresent()) {
            throw new ObjectNotFoundException(HttpStatus.NOT_FOUND, "Customer of id " + id + " not found");
        }
        updateData(newObj.get(), obj);
        customerRepository.save(newObj.get());
    }

    private void updateData(Customer newObj, Customer obj) {
        newObj.setName(obj.getName());
        newObj.setEmail(obj.getEmail());
        newObj.setGender(obj.getGender());
    }

    public void delete(Long id) {
        Optional<Customer> newObj = findById(id);
        if (!newObj.isPresent())
            throw new ObjectNotFoundException(HttpStatus.NOT_FOUND, "Customer of id " + id + " not found");
        customerRepository.deleteById(id);
    }

    public List<Customer> findAll() {
        return customerRepository.findAllByOrderByNameAsc();
    }

    public List<Customer> findByFilters(String name, String email, String gender) {
        List<Customer> customers = new ArrayList<>();
        if (name != null) {
            Optional<Customer> c = customerRepository.findByName(name);
            customers.add(c.get());
            return customers;
        } else if (email != null) {
            Optional<Customer> c = customerRepository.findByEmail(email);
            customers.add(c.get());
            return customers;
        } else if (gender != null) {
            List<Customer> listCustomerGender = customerRepository.findAllByGender(gender);
            customers.addAll(listCustomerGender);
            return customers;
        } else {
            return customerRepository.findAllByOrderByNameAsc();
        }
    }

    public Page<Customer> findPaginated(Integer page, Integer qtyPerPage, String orderBy, String direction) {
        return customerRepository.findAll(PageRequest.of(page, qtyPerPage, Sort.Direction.valueOf(direction), orderBy));
    }

    public Optional<Customer> findById(Long id) {
        Optional<Customer> c = customerRepository.findById(id);
        if (c.isPresent()) {
            c.get().setAddresses(addressRepository.findAllByCustomerId(c.get().getId()));
            return c;
        }
        throw new ObjectNotFoundException(
                HttpStatus.NOT_FOUND, "Customer of id " + id + " not found");
    }

    public Customer findByName(String name) {
        Optional<Customer> c = customerRepository.findByName(name);
        if (c.isPresent()) {
            c.get().setAddresses(addressRepository.findAllByCustomerId(c.get().getId()));
            return c.get();
        }
        throw new ObjectNotFoundException(
                HttpStatus.NOT_FOUND, "Customer of name " + name + " not found");
    }

    public Customer findByEmail(String email) {
        Optional<Customer> c = customerRepository.findByEmail(email);
        if (c.isPresent()) {
            c.get().setAddresses(addressRepository.findAllByCustomerId(c.get().getId()));
            return c.get();
        }
        throw new ObjectNotFoundException(
                HttpStatus.NOT_FOUND, "Customer of email " + email + " not found");
    }

    public List<Customer> findByGender(String gender) {
        List<Customer> lista = customerRepository.findAllByGender(gender);
        if (!lista.isEmpty()) {
            lista.stream().forEach(l -> l.setAddresses(addressRepository.findAllByCustomerId(l.getId())));
            return lista;
        }
        throw new ObjectNotFoundException(
                HttpStatus.NOT_FOUND, "Customers of gender " + gender + " not found");
    }
}