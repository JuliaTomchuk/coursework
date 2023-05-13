package org.tms.travel_agency.domain;

import java.math.BigDecimal;

public enum RoomTypesByOccupancy {
    SINGLE(new BigDecimal(100.60)), DOUBLE(new BigDecimal(180.60)), TWIN(new BigDecimal(180.60)), TRIPLE(new BigDecimal(300.60)), QUAD(new BigDecimal(460.80));
    public BigDecimal price;

    RoomTypesByOccupancy(BigDecimal price) {
        this.price = price;
    }
}
