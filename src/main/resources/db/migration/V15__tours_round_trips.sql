create table tours_round_trips
(
    tour_id        uuid not null
        constraint fkpsyh2ho8fpy3q606okamu180x
            references tours,
    round_trips_id uuid not null
        constraint uk_37ulkglpjujlwxq8wv1jsqp2p
            unique
        constraint fkian2d45o68yj3xeswmo6o6dmr
            references round_trips
);
