package br.com.app.spring.swagger;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "messages", method = GET, produces = TEXT_PLAIN_VALUE)
public class MessagesController {

	@ApiOperation(value = "greetings", httpMethod = "GET", nickname = "greetings", produces = TEXT_PLAIN_VALUE)
	@ApiResponse(code = 200, message = "Success")
	@RequestMapping(value = "greetings")
	public String greetings() {
		return "Welcome";
	}

	@ApiOperation(value = "greetingsUser", httpMethod = "GET", nickname = "greetingsUser", produces = TEXT_PLAIN_VALUE)
	@ApiImplicitParam(name = "userName", required = true, value = "User's name", dataType = "java.lang.String", paramType = "query")
	@ApiResponse(code = 200, message = "Success")
	@RequestMapping(value = "greetingsUser")
	public String greetings(@RequestParam("userName") final String userName) {
		return this.greetings() + " " + userName + "!";
	}

}
