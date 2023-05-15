create table carts
(
    id      uuid not null
        primary key,
    user_id uuid
        constraint uk_c6u0lfnev807wgbvdgxqmd15
            unique
        constraint fkrpck6ebyup9rpk05g0j72gxia
            references accounts
);
