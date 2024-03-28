package com.gabriel.course.projectapi2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

	@Bean
	public OpenAPI openApi(){
		return new OpenAPI().info(
				new Info()
				.title("API REST - Spring")
				.description("Api de gerenciamento para estacionamento")
				.version("v1.0")
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
				.contact(new Contact().name("Gabriel Silva Magalhães").email("gabiles278@gmail.com"))
				);
	}
}
