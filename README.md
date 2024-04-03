# A reference implementation of Spring Boot and GraphQL based API

## Purpose of this repository

This is a reference application showing an implementation of GraphQL API written using Spring Boot framework. It also showcases various features of GraphQL like directives, data-loaders, subscriptions etc.

## Details

This application showcases below features/operations:

- GraphQL schemas, queries, mutations
- Embedded H2 database against which the queries, mutations will be executed
- Spring Boot Data repositories
- GraphQL query directives for field level authorizations
- Subscriptions
- Built-in GraphQL explorer and query tool: GraphiQL

### GraphQL Schemas
These are stored inside 'src/main/resources' folder. Their extension is `.graphqls`. Any new schema to be added should have that extension. Spring boot auto-detects them and loads them up at application startup

### Embedded H2 database
H2 database has been bundled with this application. After the application loads up the H2 DB starts up automatically and its console is available at http://localhost:8090/h2-console. Login credentials are inside application.yml configuration file. 

The database would start up with 2 tables `EMPLOYEE` and `DEPARTMENT`. You can explore those tables by using aforementioned H2 web console. Schema for table is placed inside `/src/main/resources/schema.sql`, Insert statements for sample data inside `/src/main/resources/data.sql`. Spring JPA Hibernate dependencies auto-detec and load these scripts at startup

### Spring Boot Data Repositories
This application uses Spring Boot JPA repositories ti communicate with H2 database. Repository interfaces are located inside `repository` package. These repositories provide sample queries like `find`, `findAll` or `save` out of the box.

### GraphQL query directives
This application showcases GraphQL directives which are keywords that can be embedded inside query which will cause some effect like filtering or alter the structure of the response. These keywords start with `@` symbol and they're declared inside query definition inside the schema files. They have an associated action written as code. This applications uses basic authentication via in-memory user repository to authenticate /graphql endpoints. There are two users created, one with admin role and other with read-only role. Configuration can be found inside config.SecurityConfig. **Please note**: the security configuration is for the demo purpose only and cannot be considered as a production-ready setup at all

### Subscriptions
GraphQL subscriptions are a mechanism through which consumers can subscribe to a real-time stream of changes as they happen on a data entity. You can try out this feature from GraphiQL tool by issuing a special query as follows 

```
subscription employeeSubscription {
    employeeSubscription {
        address
        id
        name
        department {
            id
            name
        }
    }
}
```
From above query we would demand changes to Employee entity. In another browser tab you can hit a Employee creation query or mutation query. As soon as an employee record is created in backend, the subscription query will show that in the result window in the earlier tab.

### Built-in GraphQL explorers and query tools
GraphQL as many visualizers or explorers like Voyager, GraphiQL or Playground. With this application GraphiQL is built-in. It can be accessed from http://localhost:8090/graphiql URL. This tool lets us explore all the schema models exposed by the API and provies a query tool wherein we can write and submit GraphQL queries to test out our API

## How to run the application
The application can be run as a regular spring-boot java application via IDE or from command-line without any JVM options or arguments.

Once the application has started you can open GraphQL query console at http://localhost:8090/graphiql. You can fire a sample query as follows.

```
query MyQuery {
    allEmployees {
        address
        id
        name
        department {
            id
            name
        }
        phoneNumber
    }
}
```