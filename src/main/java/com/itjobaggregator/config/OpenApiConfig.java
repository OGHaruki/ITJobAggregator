package com.itjobaggregator.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                description = "OpenApi documentation for IT Job Aggregator",
                title = "OpenApi specification - IT Job Aggregator",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production ENV",
                        url = "http://139.59.149.61:8080"
                )
        }
)
public class OpenApiConfig {
}
