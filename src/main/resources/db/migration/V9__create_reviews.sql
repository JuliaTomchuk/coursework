create table reviews
(
    id       uuid not null
        primary key,
    date     date,
    message  oid,
    rating   integer,
    hotel_id uuid
        constraint fkb9igk5exfb4knqklcvka6cdhx
            references hotels,
    user_id  uuid
        constraint fks4q32xhontuvab6fe256358os
            references accounts,
    constraint uk_n8yrp8jpo1v3npx4t1chuoqq
        unique (date, hotel_id, user_id)
);
