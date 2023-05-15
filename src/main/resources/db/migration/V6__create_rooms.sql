create table rooms
(
    id                 uuid not null
        primary key,
    booked             boolean,
    pre_booked         boolean,
    price              numeric(19, 2),
    type               varchar(255),
    board_bases        integer,
    check_in           date,
    check_out          date,
    num_of_tourist     integer,
    number             integer,
    types_by_occupancy integer,
    types_by_view      integer,
    hotel_id           uuid
        constraint fkp5lufxy0ghq53ugm93hdc941k
            references hotels,
    constraint ukj1e2icfdjrv3xpl1xftqpq243
        unique (number, hotel_id)
);
