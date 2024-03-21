package com.ibm.gqldemo.graphql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "graphql", name = "fieldAuthentication", havingValue = "enabled")
public class GraphQlAuthDirectiveConfig {
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        log.info("Enabling field level authentication");
        return builder -> builder.directive("auth", new GraphQlAuthDirective());
    }
}
