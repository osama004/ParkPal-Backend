package at.technikum.parkpalbackend.model;

import jakarta.persistence.*;
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
    private User user;

    private LocalDateTime uploadDate;

    private byte[] file;
}
