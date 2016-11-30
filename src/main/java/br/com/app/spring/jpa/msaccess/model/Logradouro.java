package br.com.app.spring.jpa.msaccess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log_logradouro")
public class Logradouro {

	@Id
	@Column(name = "LOG_NU")
	private Integer id;

	@Column(name = "UFE_SG")
	private String estado;

	@Column(name = "TLO_TX")
	private String endereco;

	@Column(name = "LOG_NO")
	private String complemento;

	@Column(name = "CEP")
	private String cep;

	@ManyToOne
	@JoinColumn(name = "LOC_NU")
	private Localidade localidade;

	@ManyToOne
	@JoinColumn(name = "BAI_NU_INI")
	private Bairro bairro;
}
