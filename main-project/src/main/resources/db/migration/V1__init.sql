create table public.client
(
    id bigserial primary key not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    middle_name varchar(255),
    client_id uuid not null unique
);

create table public.account
(
    id bigserial primary  key not null,
    client_id bigint not null references public.client(id) on delete cascade on update restrict,
    account_type varchar(50) not null,
    balance numeric(10, 2) not null default 0.00 check (balance >= 0)
);

create table public.transaction
(
    id bigserial primary key not null,
    account_id bigint not null references public.account(id) on delete cascade on update restrict,
    amount numeric(10, 2) not null,
    datetime timestamp not null default now()
);

create table public.data_source_error_log
(
    id bigserial primary key not null,
    stack_trace text not null,
    message text not null,
    signature text not null
);