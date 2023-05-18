create table addresses
(
    id     uuid not null
        primary key,
    city   varchar(255),
    home   varchar(255),
    street varchar(255),
    constraint uk_q8x5f0t9qvbb9n7q81ps5upri
        unique (city, home, street)
);
