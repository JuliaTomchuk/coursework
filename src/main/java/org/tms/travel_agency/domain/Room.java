package org.tms.travel_agency.domain;


import lombok.Data;
import lombok.NoArgsConstructor;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
@Data
@NoArgsConstructor
@Entity
@Table(name = "rooms",uniqueConstraints = { @UniqueConstraint(columnNames = { "number", "hotel_id" }) })
public class Room extends TourProduct{

    @Id
    @GeneratedValue
    private UUID id;

    private Integer number;
    private Integer numOfTourist;
    private RoomTypesByOccupancy typesByOccupancy;
    private RoomTypesByView typesByView;

    private LocalDate checkIn;
    private LocalDate checkOut;
     private BoardBasisTypes boardBases;
    @ManyToOne
    private Hotel hotel;


    public Room(Integer number, RoomTypesByOccupancy typesByOccupancy, RoomTypesByView typesByView, Hotel hotel) {
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



    protected BigDecimal calculatePrice() {
        BigDecimal basicPriceOfRoomPerDay = hotel.getBasicPriceOfRoomPerDay();
        long numOfDays = checkOut.toEpochDay()-checkIn.toEpochDay();
        BigDecimal priceForView = calculatePriceByView();
        BigDecimal priceForOccupancy= calculatePriceByOccupancy();
        BigDecimal boardBasisPricePerDay = calculateBoardBasisPricePerDay();
        return basicPriceOfRoomPerDay.add(priceForOccupancy).add(priceForView).add(boardBasisPricePerDay).multiply(BigDecimal.valueOf(numOfDays));

    }


    protected void book() {
        booked=true;
    }


    protected void cancelBooking() {
     booked=false;
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
    private BigDecimal calculateBoardBasisPricePerDay(){
        BigDecimal price = new BigDecimal(0.0);
       switch (boardBases){
           case BED_AND_BREAKFAST -> new BigDecimal(100.9);
           case HALF_BOARD -> new BigDecimal(250.8);
           case FULL_BOARD -> new BigDecimal(350.6);
           case All_INCLUSIVE -> new BigDecimal(550.80);
           case ULTRA_All_INCLUSIVE -> new BigDecimal(690.9);
       }
      return price.multiply(BigDecimal.valueOf(numOfTourist));

    }
}
