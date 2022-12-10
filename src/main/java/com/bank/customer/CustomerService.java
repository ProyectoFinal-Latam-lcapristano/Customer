package com.bank.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService implements ICustomerService{

	@Autowired
	private CustomerRepository repository;
	
	@Override
	public Flux<Customer> findAll() {
		return repository.findAll();
	}

	@Override
	public Mono<Customer> findById(String id) {
		return repository.findById(id);
	}

	@Override
	public Mono<Customer> save(Customer customer) {
		return repository.save(customer);
	}

	@Override
	public Mono<Void> delete(String id) {
		return repository.deleteById(id);
	}

}
