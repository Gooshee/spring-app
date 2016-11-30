package br.com.app.spring.jpa.msaccess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log_localidade")
public class Localidade {

	@Id
	@Column(name = "LOC_NU")
	private Integer id;

	@Column(name = "UFE_SG")
	private String estado;

	@Column(name = "LOC_NO")
	private String nome;

	@Column(name = "CEP")
	private String cep;

	@OneToOne
	@JoinColumn(name = "LOC_NU_SUB")
	private Localidade subLocalidade;

	@Column(name = "MUN_NU")
	private Integer ibgeCode;

}
