create table accounts
(
    id              uuid not null
        primary key,
    age             integer,
    first_name      varchar(255),
    passport_number varchar(255),
    password        varchar(255),
    patronymic      varchar(255),
    role            integer,
    second_name     varchar(255),
    username        varchar(255)
        constraint uk_22fvt0prnafepjuqjhs2da68e
            unique
);

