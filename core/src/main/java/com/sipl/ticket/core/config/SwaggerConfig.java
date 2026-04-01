package com.sipl.ticket.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@EnableSwagger2
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${app.name:Vendor Collaboration Service}")
    private String applicationName;

    @Value("${app.version:1.0.0}")
    private String applicationVersion;

    @Value("${app.description:Service for managing vendor collaborations and communications}")
    private String applicationDescription;

    @Value("${app.build.timestamp:NA}")
    private String buildTimestamp;


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                // ✅ THIS LINE FIXES THE ERROR
                .ignoredParameterTypes(Void.class)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sipl.ticket"))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Vendor Collaboration Application APIs")
                .description(
                        String.format("Name: %s%nDescription: %s%nVersion: %s%nBuild Timestamp: %s%n",
                                applicationName, applicationDescription, applicationVersion,
                                buildTimestamp)).version(applicationVersion)
                .license("MIT License").licenseUrl("http://opensource.org/licenses/MIT")
                .contact(new Contact("Suraj informatics", "https://www.surajinformatics.com", "sales@surajinformatics.com"))
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));
    }

}
