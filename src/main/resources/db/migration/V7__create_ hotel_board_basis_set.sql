create table hotel_board_basis_set
(
    hotel_id        uuid not null
        constraint fkeij27jwvb0tjxfr1cbwp33di8
            references hotels,
    board_basis_set integer
);
