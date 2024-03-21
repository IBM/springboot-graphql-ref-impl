package com.ibm.gqldemo.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class GraphQlAuthDirective implements SchemaDirectiveWiring {
    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        String fieldName = environment.getFieldDefinition().getName();
        String targetRole = String.valueOf(environment.getAppliedDirective("auth").getArgument("role"));
        DataFetcher originalDataFetcher = environment.getFieldDataFetcher();
        DataFetcher newDataFetcher = new AuthorizingFieldDataFetcher(originalDataFetcher, targetRole, fieldName);
        return environment.setFieldDataFetcher(newDataFetcher);
    }
}

@Slf4j
class AuthorizingFieldDataFetcher implements DataFetcher {

    private DataFetcher originalDataFetcher;
    private String authorizedRole;
    private String fieldName;

    public AuthorizingFieldDataFetcher(DataFetcher originalDataFetcher, String authorizedRole, String fieldName) {

        this.originalDataFetcher = originalDataFetcher;
        this.authorizedRole = authorizedRole;
        this.fieldName = fieldName;
    }
    @Override
    public Object get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_"+authorizedRole))) {
                return originalDataFetcher.get(dataFetchingEnvironment);
            } else {
                log.warn("User {} is not authorized to access field: {}", authentication.getName(), fieldName);
                return null;
            }
        } else {
            throw new RuntimeException(String.format("No authentication information found for current user %s wishing to access restricted field %s", authentication.getName(), fieldName));
        }
    }
}