package br.com.app.spring.jpa.msaccess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log_faixa_bairro")
public class FaixaBairro implements Serializable {

	private static final long serialVersionUID = 1421929401399532102L;

	@Id
	private Integer id;

	@MapsId
	@OneToOne
	@JoinColumn(name = "BAI_NU")
	private Bairro bairro;

	@Column(name = "FCB_CEP_INI")
	private String cepIni;

	@Column(name = "FCB_CEP_FIM")
	private String cepFim;

}
