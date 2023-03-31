package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue
    private UUID id;
    @NaturalId
    private LocalDateTime date;
    @NaturalId
    @OneToOne
    private User user;
    @Lob
    private String message;

    @ManyToOne
    private Hotel hotel;

    public Review(LocalDateTime date, String message,  Hotel hotel) {
        this.date = date;
        this.message = message;

        this.hotel = hotel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;
        return getDate().equals(review.getDate()) && getUser().equals(review.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getUser());
    }
}
