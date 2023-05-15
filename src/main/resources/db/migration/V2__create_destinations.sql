create table destinations
(
    id          uuid not null
        primary key,
    description oid,
    name        varchar(255)
        constraint uk_6puuu9y8lbj3njjoai2myy4io
            unique
);
