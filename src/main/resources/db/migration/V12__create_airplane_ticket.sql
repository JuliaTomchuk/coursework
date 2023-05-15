create table airplane_ticket
(
    id                   uuid not null
        primary key,
    booked               boolean,
    pre_booked           boolean,
    price                numeric(19, 2),
    type                 varchar(255),
    airplane_seats_types integer,
    number_of_seat       integer,
    one_way_flight_id    uuid
        constraint fkpxml0x54979x1er4pi3vupnm2
            references one_way_flights
);
