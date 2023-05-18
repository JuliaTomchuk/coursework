create table hotels
(
    id                          uuid not null
        primary key,
    basic_price_of_room_per_day numeric(19, 2),
    description                 oid,
    name                        varchar(255),
    type_by_stars               integer,
    type_by_target_market       integer,
    address_id                  uuid
        constraint fkr5xj11kvydj8pj000hv3i2lbi
            references addresses,
    region_id                   uuid
        constraint fk4uini94v65rd92d48jbwjg1n5
            references regions,
    constraint uk_d26gqwdvujkyiyusmepqyg8d1
        unique (address_id, region_id)
);
