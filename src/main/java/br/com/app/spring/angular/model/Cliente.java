package br.com.app.spring.angular.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created on 15/01/2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@EqualsAndHashCode
public class Cliente extends ResourceSupport {

	@Id
	@Field("id")
	private String identifier;

	@NotNull
	private String nome;

	@NotNull
	private Long cpf;

	@Transient
	@Override
	public Link getLink(final String rel) {
		return super.getLink(rel);
	}

	@Transient
	@Override
	public List<Link> getLinks() {
		return super.getLinks();
	}
}
