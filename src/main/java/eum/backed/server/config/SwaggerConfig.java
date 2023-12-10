package eum.backed.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("eum.backed.server"))
                .paths(PathSelectors.any())
                .build().pathMapping("/");
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot Rest API Documentation")
                .description("EUM Server: community, pay API")
                .version("0.1")
                .build();
    }

    private SecurityScheme apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private springfox.documentation.spi.service.contexts.SecurityContext securityContext() {
        return springfox.documentation.spi.service.contexts.SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("Authorization", new AuthorizationScope[0])))
                .forPaths(PathSelectors.any())
                .build();
    }


}
