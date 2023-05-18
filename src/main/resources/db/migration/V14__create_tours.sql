create table tours
(
    id             uuid not null
        primary key,
    booked         boolean,
    pre_booked     boolean,
    price          numeric(19, 2),
    type           varchar(255),
    booking_number bigint,
    num_of_tourist integer,
    region_id      uuid
        constraint fk2u5q9sxb0tum5g87gya69eoi6
            references regions,
    room_id        uuid
        constraint fkb53mgx228g51eqxxvtco7r9c3
            references rooms
);
