package br.com.app.spring.jpa.msaccess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log_grande_usuario")
public class GrandeUsuario {

	@Id
	@Column(name = "GRU_NU")
	private Integer id;

	@Column(name = "UFE_SG")
	private String estado;

	@Column(name = "GRU_ENDERECO")
	private String endereco;

	@Column(name = "CEP")
	private String cep;

	@ManyToOne
	@JoinColumn(name = "LOC_NU")
	private Localidade localidade;

	@ManyToOne
	@JoinColumn(name = "BAI_NU")
	private Bairro bairro;

}
