package com.bank.customer;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping()
public class CustomerController {
	@Autowired
	private ICustomerService service;
	
	@GetMapping("/list")
	public Flux<Customer> listar() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Mono<Customer> detalle(@PathVariable String id) {
		return service.findById(id);
	}

	@PostMapping("/save")
	public Mono<ResponseEntity<Map<String, Object>>> save(@RequestBody Customer customer) {
		Map<String, Object> result = new HashMap<String, Object>();

		return service.save(customer).map(p -> {
			result.put("id", customer.getId().toString());
			result.put("name", customer.getName());
			result.put("type", customer.getType());
			result.put("Mensaje", "Customero guardado correctamente");
			result.put("status", true);
			return ResponseEntity.created(URI.create("/save".concat(p.getId())))
					.contentType(MediaType.APPLICATION_JSON).body(result);
		}).onErrorResume(err -> {
			return Mono.just(err).cast(WebExchangeBindException.class).flatMap(e -> Mono.just(e.getFieldErrors()))
					.flatMapMany(errs -> Flux.fromIterable(errs))
					.map(fieldError -> "Campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
					.collectList().flatMap(list -> {
						result.put("Errors", list);
						result.put("Status", HttpStatus.BAD_REQUEST.value());
						return Mono.just(ResponseEntity.badRequest().body(result));
					});
		});
	}
	
	@DeleteMapping("/{id}")
	public Mono<Void> delete(@PathVariable String id) {
		return service.delete(id);
	}
}
