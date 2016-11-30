package br.com.app.spring.jpa.msaccess.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log_bairro")
public class Bairro {

	@Id
	@Column(name = "BAI_NU")
	private Integer id;

	@Column(name = "BAI_NO")
	private String nome;

}
