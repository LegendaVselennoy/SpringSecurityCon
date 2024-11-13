create table if not exists dz.customer
(
    id bigserial primary key not null ,
    email varchar(20),
    pwd varchar(20),
    role varchar(15)
);