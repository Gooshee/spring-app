package br.com.app.spring.angular.controller;

import br.com.app.spring.angular.model.Cliente;
import br.com.app.spring.angular.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created on 15/01/2016.
 */
@RestController
public class ClienteController {

	/**
	 * The Service.
	 */
	private final ClienteService service;

	/**
	 * Instantiates a new Cliente controller.
	 *
	 * @param service the service
	 */
	public ClienteController(final ClienteService service) {
		this.service = service;
	}

	/**
	 * Save cliente.
	 *
	 * @param cliente the cliente
	 * @return the cliente
	 */
	@RequestMapping(value = "save", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public Cliente save(@RequestBody final Cliente cliente) {
		return this.service.save(cliente);
	}

	/**
	 * Remove.
	 *
	 * @param id the id
	 */
	@RequestMapping(value = "remove", method = POST)
	public void remove(@RequestParam("id") final String id) {
		this.service.remove(id);
	}

	/**
	 * Find all list.
	 *
	 * @return the list
	 */
	@RequestMapping(value = "clientes", method = GET, produces = APPLICATION_JSON_VALUE)
	public List<Cliente> findAll() {
		return this.service.findAll();
	}

	/**
	 * Find by id cliente.
	 *
	 * @param id the id
	 * @return the cliente
	 */
	@RequestMapping(value = "/clientes/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
	public Cliente findById(@PathVariable("id") final String id) {
		return this.service.findById(id);
	}

	/**
	 * Client count long.
	 *
	 * @param cpf the cpf
	 * @return the long
	 */
	@RequestMapping(value = "/count/{cpf}", method = GET)
	public ResponseEntity<Long> clientCount(@PathVariable("cpf") final Long cpf) {
		return ResponseEntity.ok(this.service.countByCpf(cpf));
	}

}
