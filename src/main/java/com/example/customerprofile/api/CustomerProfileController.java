package com.example.customerprofile.api;

import com.example.customerprofile.domain.CustomerProfile;
import com.example.customerprofile.domain.CustomerProfileService;
import com.example.customerprofile.domain.NewCustomerProfile;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@OpenAPIDefinition(
        info = @Info(
                title = "Customer Profile Management API",
                version = "1.0"),
        tags = @Tag(
                name = "Customer Profile REST API"))
@CrossOrigin
@RestController
@RequestMapping("/api/customer-profiles")
class CustomerProfileController {

    private final CustomerProfileService service;

    CustomerProfileController(CustomerProfileService service) {
        this.service = service;
    }

    @Operation(summary = "Saves provided customer profile.", method = "POST", tags = "Customer Profile CRUD")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Customer profile successfully saved.",
                    headers = @Header(
                            name = "Location",
                            description = "Contains path which can be used to retrieve saved profile. Last element is it's ID.",
                            required = true,
                            schema = @Schema(type = "string"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Passed customer profile is invalid."
            )
    })
    @PostMapping("")
    ResponseEntity<Void> create(@Valid @RequestBody CustomerProfileCreateRequest request) {
        var newCustomerProfile = fromRequest(request);

        var customerProfile = service.create(newCustomerProfile);

        return ResponseEntity.created(toLocationUri(customerProfile.id())).build();
    }

    @Operation(summary = "Get customer profile.", method = "GET", tags = "Customer Profile CRUD")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer profile retrieved successfully."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Customer profile not found."
            )
    })
    @GetMapping("/{id}")
    ResponseEntity<CustomerProfileResponse> get(@PathVariable("id") Long id) {
        var customerProfile = service.getById(id);

        return customerProfile
                .map(profile -> ok(toResponse(profile)))
                .orElseGet(() -> notFound().build());
    }

    private CustomerProfileResponse toResponse(CustomerProfile customerProfile) {
        return new CustomerProfileResponse(
                customerProfile.id(),
                customerProfile.firstName(),
                customerProfile.lastName(),
                customerProfile.email()
        );
    }

    private static NewCustomerProfile fromRequest(CustomerProfileCreateRequest request) {
        return new NewCustomerProfile(
                request.firstName(),
                request.lastName(),
                request.email()
        );
    }

    private URI toLocationUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
