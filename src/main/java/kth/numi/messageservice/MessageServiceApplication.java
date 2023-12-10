package kth.numi.messageservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@Controller
@OpenAPIDefinition(
		info = @Info(
				title = "Message service",
				version = "1.0.0",
				description = "APIs for Message service for Patient Journal website",
				termsOfService = "none for now"
		)
)
public class MessageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageServiceApplication.class, args);
	}

	@Bean
	public GroupedOpenApi customOpenAPI() {
		return GroupedOpenApi.builder()
				.group("Message_service_APIs")
				.pathsToMatch(
						"/message/**" )
				.build();
	}
}