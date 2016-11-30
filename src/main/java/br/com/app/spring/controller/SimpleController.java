package br.com.app.spring.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Validated
@RestController
@RequestMapping(method = GET, value = "simple")
public class SimpleController {

	@RequestMapping("someMessage/{code}")
	public String someMessage(@PathVariable final String code, @Valid final RequestParams params) {
		return code + " - " + params.toString();
	}

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class RequestParams implements Serializable {
	private static final long serialVersionUID = 5859301753575008078L;
	@NotNull
	private Integer id;
	private String name;
}

