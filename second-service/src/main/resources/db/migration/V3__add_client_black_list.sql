create table public.client_black_list
(
    id        bigserial primary key not null,
    client_id uuid                  not null unique
);