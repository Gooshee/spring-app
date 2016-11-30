package br.com.app.spring.jpa.msaccess.domain;

import br.com.app.spring.jpa.msaccess.model.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Zipcode {

	private Integer postalCode;
	private String street;
	private String neighborhood;
	private String city;
	private String state;
	private Integer ibgeCode;

	public Zipcode() {
		this.neighborhood = "";
		this.city = "";
	}

	public Zipcode(final Localidade localidade) {
		this();
		this.postalCode = Integer.valueOf(localidade.getCep());
		this.state = localidade.getEstado();
		this.defineIBGECodeAndCity(localidade);
	}

	public Zipcode(final Logradouro logradouro) {
		this();
		this.postalCode = Integer.valueOf(logradouro.getCep());
		this.state = logradouro.getEstado();
		this.street = logradouro.getEndereco();
		Optional.ofNullable(logradouro.getLocalidade()).ifPresent(this::defineIBGECodeAndCity);
		Optional.ofNullable(logradouro.getBairro()).ifPresent(bairro -> this.neighborhood = bairro.getNome());
	}

	public Zipcode(final GrandeUsuario gru) {
		this();
		this.postalCode = Integer.valueOf(gru.getCep());
		this.state = gru.getEstado();
		this.street = gru.getEndereco();
		Optional.ofNullable(gru.getLocalidade()).ifPresent(this::defineIBGECodeAndCity);
		Optional.ofNullable(gru.getBairro()).ifPresent(bairro -> this.neighborhood = bairro.getNome());
	}

	public Zipcode(final CPC cpc, final List<FaixaBairro> faixas) {
		this();
		final String cep = cpc.getCep();
		this.postalCode = Integer.valueOf(cep);
		this.state = cpc.getEstado();
		this.street = cpc.getEndereco();
		final Optional<FaixaBairro> faixaOptional = faixas.stream().filter(faixa -> cep.compareTo(faixa.getCepIni()) >= 0 && cep.compareTo(faixa.getCepFim()) <= 0).findFirst();
		faixaOptional.ifPresent(faixa -> this.neighborhood = faixa.getBairro().getNome());
		Optional.ofNullable(cpc.getLocalidade()).ifPresent(this::defineIBGECodeAndCity);
	}

	private void defineIBGECodeAndCity(final Localidade localidade) {
		this.city = localidade.getNome();
		this.ibgeCode = Optional.ofNullable(localidade.getIbgeCode()).orElseGet(() -> {
			final Localidade sub = localidade.getSubLocalidade();
			this.city = sub.getNome();
			return sub.getIbgeCode();
		});
	}
}
