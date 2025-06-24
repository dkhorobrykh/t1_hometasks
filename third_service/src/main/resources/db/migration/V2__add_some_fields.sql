alter table public.account_remove_arrest_request
    add result  boolean,
    add message text;

alter table public.client_unblock_request
    add result  boolean,
    add message text;