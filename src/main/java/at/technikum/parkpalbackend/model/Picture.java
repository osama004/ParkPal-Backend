package at.technikum.parkpalbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

@Entity(name = "pictures")
public class Picture {

    @Id
    @UuidGenerator
    @Column(name = "picture_id")
    private String pictureId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    @NotNull(message="Picture must belong to a User. Pls add a User")
    private User user;

    @NotBlank(message = "File must have an uploadDate")
    private LocalDateTime uploadDate;

    @NotBlank(message = "File must exist")
    private byte[] file;
}
