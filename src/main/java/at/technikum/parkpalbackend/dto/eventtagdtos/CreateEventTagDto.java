package at.technikum.parkpalbackend.dto.eventtagdtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Valid
public class CreateEventTagDto {

    @NotBlank(message = "Event Tag Name cannot be empty.")
    @Length(min = 3, max = 50, message = "Event Tag Name must be between 3 and 50 characters.")
    private String name;

    @Builder.Default
    private Set<String> eventIds = new HashSet<>();
}

