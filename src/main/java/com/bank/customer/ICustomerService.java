package com.bank.customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {
	public Flux<Customer> findAll();
	public Mono<Customer> findById(String id);
	public Mono<Customer> save(Customer customer);
	public Mono<Void> delete(String id);
}
