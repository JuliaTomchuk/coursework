package org.tms.travel_agency.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tms.travel_agency.domain.TourProduct;

import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private UUID id;
    private String login;
    private List<TourProduct> tourProducts;

}
