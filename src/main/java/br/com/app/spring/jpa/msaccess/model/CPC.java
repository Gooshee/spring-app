package br.com.app.spring.jpa.msaccess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log_cpc")
public class CPC {

	@Id
	@Column(name = "CPC_NU")
	private Integer id;

	@Column(name = "UFE_SG")
	private String estado;

	@Column(name = "CPC_ENDERECO")
	private String endereco;

	@Column(name = "CEP")
	private String cep;

	@ManyToOne
	@JoinColumn(name = "LOC_NU")
	private Localidade localidade;

	@Column(name = "CPC_NO")
	private String nome;

}
