package org.tms.travel_agency.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Data
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room extends TourProduct{

    @Id
    @GeneratedValue
    private UUID id;

   // @NaturalId
    @Column(unique = true)
    private Integer number;
    private RoomTypesByOccupancy typesByOccupancy;
    private RoomTypesByView typesByView;

    private LocalDate checkIn;
    private LocalDate checkOut;
    private boolean isBooked;
    @OneToMany()
    private List<BoardBasis> boardBases = new ArrayList<>();
    @ManyToOne
    //@NaturalId
     private Hotel hotel;

    public Room(Integer number, RoomTypesByOccupancy typesByOccupancy, RoomTypesByView typesByView, Hotel hotel,boolean isBooked ) {
        this.number = number;
        this.typesByOccupancy = typesByOccupancy;
        this.typesByView = typesByView;
        this.hotel = hotel;


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        return getNumber().equals(room.getNumber()) && getHotel().equals(room.getHotel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getHotel());
    }


    @Override
    protected BigDecimal calculatePrice() {
        BigDecimal basicPriceOfRoomPerDay = hotel.getBasicPriceOfRoomPerDay();
        long numOfDays = checkOut.toEpochDay()-checkIn.toEpochDay();
        BigDecimal priceForView = calculatePriceByView();
        BigDecimal priceForOccupancy= calculatePriceByOccupancy();
        return basicPriceOfRoomPerDay.add(priceForOccupancy).add(priceForView).multiply(BigDecimal.valueOf(numOfDays));

    }

    @Override
    protected void book() {
        isBooked=true;
    }

    @Override
    protected void cancelBooking() {
     isBooked=false;
    }
    private BigDecimal calculatePriceByView(){
        BigDecimal price= new BigDecimal(0.0);
        switch(typesByView){
            case INSIDE ->  price = new BigDecimal(15.2);
            case CITY ->  price = new BigDecimal(30.8);
            case LAND -> price =  new BigDecimal(25.7);
            case PARK ->  price = new BigDecimal(35.4);
            case GARDEN -> price =  new BigDecimal(20.9);
            case POOL ->  price = new BigDecimal(40.6);
            case MOUNTAIN -> price =  new BigDecimal(50.5);
            case SIDE_SEA ->  price = new BigDecimal(60.2);
            case SEA ->  price = new BigDecimal(70.5);

        }
        return price;
    }
    private BigDecimal calculatePriceByOccupancy(){
        BigDecimal price = new BigDecimal(0.0);
        switch(typesByOccupancy){
            case SINGLE -> price = new BigDecimal(100.60);
            case DOUBLE,TWIN -> price = new BigDecimal(180.60);
            case TRIPLE -> price= new BigDecimal(300.60);
            case QUAD -> price= new BigDecimal(460.10);
        }
        return price;
    }
}
