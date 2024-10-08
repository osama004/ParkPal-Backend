package at.technikum.parkpalbackend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

@Embeddable
public class Address {

    @NotEmpty(message = "Street and Number not found")
    @Column(length = 64)
    private String streetNumber;

    @NotEmpty(message = "Zip Code not found")
    @Min(value = 4, message = "Zip Code cant be less than 4 Characters")
    @Max(value = 10, message = "Zip Code cant be more than 10 Characters")
    @Column(length = 10)
    private String zipCode;

    @NotEmpty(message= "City not found")
    @Column(length = 64)
    private String city;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "country_id", foreignKey = @ForeignKey(name = "fk_address_2_country"))
    @JsonDeserialize(using = CountryDeserializer.class)
    private Country country;

}
