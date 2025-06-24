create table public.role
(
    id bigserial not null primary key ,
    name varchar(255) not null unique
);

create table public.user
(
    id bigserial not null primary key ,
    username varchar(255) not null unique
);

create table public.user_role
(
    user_id bigint not null references public.user(id) on delete cascade,
    role_id bigint not null references public.role(id) on delete cascade,
    primary key (user_id, role_id)
);

insert into public.role (name) values ('ROLE_USER');

insert into public.user (username) values ('main-service');

insert into public.user_role (user_id, role_id) values (
    (select id from public.user where username = 'main-service'),
    (select id from public.role where name = 'ROLE_USER')
);