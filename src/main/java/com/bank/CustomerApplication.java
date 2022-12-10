package com.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.bank.customer.Customer;
import com.bank.customer.CustomerRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Slf4j
public class CustomerApplication implements CommandLineRunner {

	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("customer").subscribe();
		Flux.just(
				new Customer("C001","Lucar Capristano","Personal"),
				new Customer("C002","Dev17 S.A.C","Empresarial"),
				new Customer("C003","Comercial Vega S.A","Empresarial")
				).flatMap(p -> {
					return repository.save(p);
				}).subscribe(p -> log.info("Insert: " + p.toString()));
	}

}
