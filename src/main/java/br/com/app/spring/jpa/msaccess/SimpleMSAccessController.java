package br.com.app.spring.jpa.msaccess;

import br.com.app.spring.jpa.msaccess.domain.Zipcode;
import br.com.app.spring.jpa.msaccess.model.*;
import br.com.app.spring.jpa.msaccess.model.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@RestController
public class SimpleMSAccessController {

	private final BairroRepository bairroRepository;
	private final CPCRepository cpcRepository;
	private final FaixaBairroRepository faixaBairroRepository;
	private final GrandeUsuarioRepository grandeUsuarioRepository;
	private final LocalidadeRepository localidadeRepository;
	private final LogradouroRepository logradouroRepository;

	public SimpleMSAccessController(final BairroRepository bairroRepository,
	                                final CPCRepository cpcRepository,
	                                final FaixaBairroRepository faixaBairroRepository,
	                                final GrandeUsuarioRepository grandeUsuarioRepository,
	                                final LocalidadeRepository localidadeRepository,
	                                final LogradouroRepository logradouroRepository) {
		this.bairroRepository = bairroRepository;
		this.cpcRepository = cpcRepository;
		this.faixaBairroRepository = faixaBairroRepository;
		this.grandeUsuarioRepository = grandeUsuarioRepository;
		this.localidadeRepository = localidadeRepository;
		this.logradouroRepository = logradouroRepository;
	}

	@RequestMapping(value = "/bairros", method = GET, produces = APPLICATION_JSON_VALUE)
	public Page<Bairro> bairros(@PageableDefault final Pageable pageable) {
		return this.bairroRepository.findAll(pageable);
	}

	@RequestMapping(value = "/cpcs", method = GET, produces = APPLICATION_JSON_VALUE)
	public Page<CPC> cpcs(@PageableDefault final Pageable pageable) {
		return this.cpcRepository.findAll(pageable);
	}

	@RequestMapping(value = "/faixasBairros", method = GET, produces = APPLICATION_JSON_VALUE)
	public Page<FaixaBairro> faixasBairros(@PageableDefault final Pageable pageable) {
		return this.faixaBairroRepository.findAll(pageable);
	}

	@RequestMapping(value = "/grandeUsuarios", method = GET, produces = APPLICATION_JSON_VALUE)
	public Page<GrandeUsuario> grandeUsuarios(@PageableDefault final Pageable pageable) {
		return this.grandeUsuarioRepository.findAll(pageable);
	}

	@RequestMapping(value = "/localidades", method = GET, produces = APPLICATION_JSON_VALUE)
	public Page<Localidade> localidades(@PageableDefault final Pageable pageable) {
		return this.localidadeRepository.findAll(pageable);
	}

	@RequestMapping(value = "/localidadesSub", method = GET, produces = APPLICATION_JSON_VALUE)
	public Page<Localidade> localidadesSub(@PageableDefault final Pageable pageable) {
		return this.localidadeRepository.findBySubLocalidadeNotNull(pageable);
	}

	@RequestMapping(value = "/logradouros", method = GET, produces = APPLICATION_JSON_VALUE)
	public Page<Logradouro> logradouros(@PageableDefault final Pageable pageable) {
		return this.logradouroRepository.findAll(pageable);
	}

	@RequestMapping(value = "/zipCodes", method = GET, produces = APPLICATION_JSON_VALUE)
	public int zipCodes() {
		log.info("Generating entries for MongoDB...");

		final List<Zipcode> entries = new ArrayList<>();

		// Localidades com apenas 1 CEP
		final List<Localidade> localidades = this.localidadeRepository.findByCepNotNull();
		final List<Zipcode> localidadesEntries = localidades.parallelStream().map(Zipcode::new).collect(Collectors.toList());
		entries.addAll(localidadesEntries);
		//		entries.addAll(localidades.parallelStream().map(Zipcode::new).collect(Collectors.toList()));

		// CEPs normais
		final List<Logradouro> logradouros = this.logradouroRepository.findAll();
		final List<Zipcode> logradourosEntries = logradouros.parallelStream().map(Zipcode::new).collect(Collectors.toList());
		entries.addAll(logradourosEntries);
		//		entries.addAll(logradouros.parallelStream().map(Zipcode::new).collect(Collectors.toList()));

		// Grandes usuarios
		final List<GrandeUsuario> grandeUsuarios = this.grandeUsuarioRepository.findAll();
		final List<Zipcode> gruEntries = grandeUsuarios.parallelStream().map(Zipcode::new).collect(Collectors.toList());
		entries.addAll(gruEntries);
		//		entries.addAll(grandeUsuarios.parallelStream().map(Zipcode::new).collect(Collectors.toList()));

		// CPCs
		final List<CPC> cpcs = this.cpcRepository.findAll();
		final List<FaixaBairro> faixas = this.faixaBairroRepository.findAll();
		final List<Zipcode> cpcEntries = cpcs.parallelStream().map(cpc -> new Zipcode(cpc, faixas)).collect(Collectors.toList());
		entries.addAll(cpcEntries);
		//		entries.addAll(cpcs.parallelStream().map(cpc -> new Zipcode(cpc, faixas)).collect(Collectors.toList()));

		log.info("{} zipcode entries was found.", entries.size());

		return entries.size();
	}

}
