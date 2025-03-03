package com.example.onlinecourses.swagger.v1;
import com.example.onlinecourses.dtos.auth.AuthRequestDTO;
import com.example.onlinecourses.swagger.requests.SignUpRequest;
import com.example.onlinecourses.swagger.responses.AuthApiResponse;
import com.example.onlinecourses.swagger.responses.EmptyDataResponse;
import com.example.onlinecourses.swagger.responses.RefreshTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class AuthSwagger {
    @Target(ElementType.METHOD) // This annotation can only be applied to methods
    @Retention(RetentionPolicy.RUNTIME) // The annotated element is retained at runtime
    @Operation(
        summary = "Sign up",
        description = "Register a new user",
        requestBody = @RequestBody(
            description = "User creation request",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SignUpRequest.class)
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sign up successful",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthApiResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = @Content)
    })
    public @interface SignUp {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Sign In",
        description = "Sign in a user",
        requestBody = @RequestBody(
            description = "User authentication request",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthRequestDTO.class)
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sign in successful",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthApiResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = @Content)
    })
    public @interface SignIn {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Sign out",
        description = "Sign out a user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sign out successful",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = EmptyDataResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = @Content)
    })
    public @interface SignOut {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
        summary = "Sign In",
        description = "Sign in a user",
        parameters = @Parameter(
            name = "refreshToken",
            description = "Refresh token",
            in = ParameterIn.COOKIE
        )
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = RefreshTokenResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = @Content)
    })
    public @interface RefreshAccessToken {
    }
}
