package br.com.app.spring.jpa.msaccess.model.repository;

import br.com.app.spring.jpa.msaccess.model.Localidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocalidadeRepository extends JpaRepository<Localidade, Integer> {

	Page<Localidade> findBySubLocalidadeNotNull(Pageable pageable);

	List<Localidade> findByCepNotNull();
}
