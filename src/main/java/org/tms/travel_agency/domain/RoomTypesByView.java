package org.tms.travel_agency.domain;

import java.math.BigDecimal;

public enum RoomTypesByView {

    SEA(new BigDecimal(100.90)), POOL(new BigDecimal(80.70)), PARK(new BigDecimal(50.60)), CITY(new BigDecimal(30.40)), GARDEN(new BigDecimal(34.89)), INSIDE(new BigDecimal(10.09)), LAND(new BigDecimal(15.15)), MOUNTAIN(new BigDecimal(120.60)), SIDE_SEA(new BigDecimal(70.70));
    public BigDecimal price;

    RoomTypesByView(BigDecimal price) {
        this.price = price;
    }
}
