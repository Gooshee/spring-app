package br.com.app.spring.angular.repository;

import br.com.app.spring.angular.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created on 15/01/2016.
 */
public interface ClienteRepository extends MongoRepository<Cliente, String> {

	/**
	 * Find by id cliente.
	 *
	 * @param identifier the identifier
	 * @return the cliente
	 */
	Cliente findByIdentifier(String identifier);

	/**
	 * Count by cpf long.
	 *
	 * @param cpf the cpf
	 * @return the long
	 */
	long countByCpf(Long cpf);

}
