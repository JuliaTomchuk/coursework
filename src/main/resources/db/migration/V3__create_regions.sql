create table regions
(
    id             uuid not null
        primary key,
    description    oid,
    name           varchar(255)
        constraint uk_8y6aseo3p845xiwhjryco5lwn
            unique,
    destination_id uuid
        constraint fkspx30vcwcj8s4pwv24uehsx0c
            references destinations
);
