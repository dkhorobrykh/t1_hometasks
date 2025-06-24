create table public.transaction_history
(
    id                 bigserial primary key not null,
    client_id          uuid,
    account_id         uuid,
    transaction_id     uuid,
    timestamp          timestamp,
    transaction_amount numeric(10, 2),
    account_balance    numeric(10, 2)
)