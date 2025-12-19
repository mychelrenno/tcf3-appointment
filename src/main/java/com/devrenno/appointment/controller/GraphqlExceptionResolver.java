package com.devrenno.appointment.controller;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import graphql.schema.DataFetchingEnvironment;

import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class GraphqlExceptionResolver implements DataFetcherExceptionResolver {
    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable ex, DataFetchingEnvironment env) {

        if (ex instanceof IllegalArgumentException) {
            GraphQLError error = GraphqlErrorBuilder.newError(env)
                    .message(ex.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();

            return Mono.just(List.of(error));
        }

        if (ex instanceof DateTimeParseException) {
            GraphQLError error = GraphqlErrorBuilder.newError(env)
                    .message("Incorrect date and time format, please use dd/MM/yyyy HH:mm")
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();

            return Mono.just(List.of(error));
        }

        return Mono.empty(); // deixa outras exceptions seguirem como INTERNAL_ERROR
    }
}
