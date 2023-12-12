package com.spring.userservice.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "SaltEdgeService API Documentation",
                description = "This API documentation provides information about the SaltEdgeService API, which is part of the Bankwards application.",
                version = "1.0",
                contact = @Contact(
                        name = "xyz",
                        email = "xyz@example.com",
                        url = "https://www.bankwards.com"
                ),
                license = @License(
                        name = "Bankwards License",
                        url = "https://www.example.com/licenses/bankwards-license"
                )
        )
)
public class OpenApiConfig {
	
}
