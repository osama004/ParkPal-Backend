package at.technikum.parkpalbackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Valid
public class VideoDto {
    private String id;

    @NotNull(message="Video must belong to a User. Please add a User")
    private String userId;

    @NotNull(message = "File must have an uploadDate")
    private LocalDateTime uploadDate;

}
