package org.tms.travel_agency.domain;


import java.math.BigDecimal;

public enum BoardBasisTypes {

       All_INCLUSIVE(new BigDecimal(550.80)), ULTRA_All_INCLUSIVE(new BigDecimal(690.90)), BED_AND_BREAKFAST(new BigDecimal(100.90)), HALF_BOARD(new BigDecimal(250.70)), FULL_BOARD(new BigDecimal(350.60));
       public BigDecimal price;

       BoardBasisTypes(BigDecimal price) {
              this.price = price;

       }
}
