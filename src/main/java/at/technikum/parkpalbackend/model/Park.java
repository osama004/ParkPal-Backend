package at.technikum.parkpalbackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

@Entity
public class Park {

    @Id
    @UuidGenerator
    @Column(name = "park_id", unique = true)
    private String parkId;

    @Column(unique = true)
    private String name;

    private String description;

    @Embedded
    private Address address;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @ToString.Exclude
    private List<Event> parkEvents = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @ToString.Exclude
    private List<Picture> parkPictures = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @ToString.Exclude
    private List<Video> parkVideos = new ArrayList<>();

    public Park addParkEvents(Event... events) {
        Arrays.stream(events).forEach(event -> this.parkEvents.add(event));
        return this;
    }

    public Park removeParkEvents(Event... events) {
        Arrays.stream(events).forEach(event -> this.parkEvents.remove(event));
        return this;
    }

    public Park addParkMedia(Picture... media) {
        Arrays.stream(media).forEach(med -> this.parkPictures.add(med));
        return this;
    }

    public Park removeParkMedia(Picture... media) {
        Arrays.stream(media).forEach(med -> this.parkPictures.remove(med));
        return this;
    }

}
