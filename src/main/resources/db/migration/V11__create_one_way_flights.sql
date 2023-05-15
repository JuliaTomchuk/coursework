create table one_way_flights
(
    id                uuid not null
        primary key,
    arrival_airport   varchar(255),
    arrival_time      timestamp,
    cabin_baggage     integer,
    checked_baggage   integer,
    departure_airport varchar(255),
    departure_time    timestamp,
    flight_number     varchar(255),
    flight_time       integer,
    price_per_hour    numeric(19, 2),
    destination_id    uuid
        constraint fkhwxtdax0wnlfqfp37owmgv0te
            references destinations,
    constraint uk_ojgcnu5f2c1sirj59n2imia7h
        unique (departure_airport, departure_time, flight_number)
);

