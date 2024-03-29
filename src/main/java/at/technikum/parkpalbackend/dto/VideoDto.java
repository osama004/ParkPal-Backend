package at.technikum.parkpalbackend.dto;

import jakarta.validation.Valid;
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
    private String videoId;

    private String userId;

    private LocalDateTime uploadDate;

}
