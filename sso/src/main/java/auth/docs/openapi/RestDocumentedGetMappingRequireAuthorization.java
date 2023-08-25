package auth.docs.openapi;

import auth.errors.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation
public @interface RestDocumentedGetMappingRequireAuthorization {

    @AliasFor(annotation = Operation.class)
    String summary() default "";

    @AliasFor(annotation = Operation.class)
    String description() default "";

    @AliasFor(annotation = Operation.class)
    ApiResponse[] responses() default {
            @ApiResponse(responseCode = RestResponseCodes.NOT_FOUND,
                    description = RestResponseCodes.NOT_FOUND_DESCRIPTION,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(responseCode = RestResponseCodes.OK,
                    description = RestResponseCodes.OK_DESCRIPTION),

            @ApiResponse(responseCode = RestResponseCodes.UNAUTHORIZED,
                    description = RestResponseCodes.UNAUTHORIZED_DESCRIPTION,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),

            @ApiResponse(responseCode = RestResponseCodes.FORBIDDEN,
                    description = RestResponseCodes.FORBIDDEN_DESCRIPTION,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    };

    @AliasFor(annotation = Operation.class)
    SecurityRequirement[] security() default {};
}
