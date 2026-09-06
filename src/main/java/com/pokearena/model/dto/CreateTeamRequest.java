package com.pokearena.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateTeamRequest(

        @NotNull
        Long trainerId,
        @NotBlank
        @Size(min = 2 , max = 30)
        String teamName,

        @NotNull
        @Size(min =1 , max = 6)
        @Valid
        List<CreateTeamMemberRequest> members
) {
}
