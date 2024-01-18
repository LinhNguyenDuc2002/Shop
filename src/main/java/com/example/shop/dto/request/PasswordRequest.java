package com.example.shop.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordRequest {
    @JsonProperty("old-password")
    private String oldPassword;

    @JsonProperty("new-password")
    @Size(min = 6, message = "Password has a minimum length of 6 characters")
    @NotNull(message = "Password cannot be null")
    private String newPassword;
}
