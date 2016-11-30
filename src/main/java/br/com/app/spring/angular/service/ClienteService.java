package br.com.app.spring.angular.service;

import br.com.app.spring.angular.controller.ClienteController;
import br.com.app.spring.angular.model.Cliente;
import br.com.app.spring.angular.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created on 15/01/2016.
 */
@Service
public class ClienteService {

	/**
	 * The Repository.
	 */
	private final ClienteRepository repository;

	/**
	 * Instantiates a new Cliente service.
	 *
	 * @param repository the repository
	 */
	public ClienteService(final ClienteRepository repository) {
		this.repository = repository;
	}

	/**
	 * Save cliente.
	 *
	 * @param cliente the cliente
	 * @return the cliente
	 */
	public Cliente save(final Cliente cliente) {
		final Cliente savedClient = this.repository.save(cliente);
		return this.createLink(savedClient);
	}

	/**
	 * Remove.
	 *
	 * @param id the id
	 */
	public void remove(final String id) {
		this.repository.delete(id);
	}

	/**
	 * Find all list.
	 *
	 * @return the list
	 */
	public List<Cliente> findAll() {
		final List<Cliente> clientes = this.repository.findAll();
		clientes.forEach(this::createLink);
		return clientes;
	}

	/**
	 * Find by id cliente.
	 *
	 * @param id the id
	 * @return the cliente
	 */
	public Cliente findById(final String id) {
		return this.repository.findByIdentifier(id);
	}

	/**
	 * Count by cpf long.
	 *
	 * @param cpf the cpf
	 * @return the long
	 */
	public long countByCpf(final Long cpf) {
		return this.repository.countByCpf(cpf);
	}

	/**
	 * Create link.
	 *
	 * @param cliente the cliente
	 * @return the cliente
	 */
	private Cliente createLink(final Cliente cliente) {
		cliente.add(linkTo(methodOn(ClienteController.class).findById(cliente.getIdentifier())).withSelfRel());
		return cliente;
	}

}
