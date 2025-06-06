alter table public.transaction
    rename column datetime to timestamp;

alter table public.account
    add status       varchar(255)   not null default 'OPEN',
    add account_id   uuid           not null default gen_random_uuid(),
    add frozenAmount numeric(10, 2) not null default 0.0;

alter table public.transaction
    add status         varchar(255) not null default 'REQUESTED',
    add transaction_id uuid         not null default gen_random_uuid();