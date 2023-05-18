create table round_trips
(
    id             uuid not null
        primary key,
    booked         boolean,
    pre_booked     boolean,
    price          numeric(19, 2),
    type           varchar(255),
    arrive_id      uuid
        constraint fk27hnpmhc5qg8ydmusqgpu99rf
            references airplane_ticket,
    depart_id      uuid
        constraint fk3aoeh5sn626wjmis8hwekod8x
            references airplane_ticket,
    destination_id uuid
        constraint fkiwes2g92oaffcgjn0hrqnugat
            references destinations
);
