create table carts_tour_product_list
(
    cart_id              uuid not null
        constraint fkhrrbtpp3xipg9pxhtnto2d4po
            references carts,
    tour_product_list_id uuid not null
        constraint uk_fik082bgndxy1kntrha6jvm9u
            unique
);
