create table public.client_unblock_request
(
    id            bigserial primary key                  not null,
    client_id     uuid                                   not null,
    client_status varchar(255)                           not null,
    timestamp     timestamp with time zone default now() not null
);

create table public.account_remove_arrest_request
(
    id             bigserial primary key                  not null,
    account_id     uuid                                   not null,
    account_status varchar(255)                           not null,
    timestamp      timestamp with time zone default now() not null
);