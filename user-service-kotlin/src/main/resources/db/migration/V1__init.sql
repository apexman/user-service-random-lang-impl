create table if not exists user_service_schema.users
(
    id                  serial primary key,
    username            varchar not null unique,
    created_at          timestamp not null,
    last_modified_at    timestamp not null
);

create table if not exists user_service_schema.contacts
(
    id               serial primary key,
    created_at       timestamp not null,
    last_modified_at timestamp not null,
    user_id          integer constraint fk_user references user_service_schema.users,
    contact_value    varchar not null unique,
    contact_type     varchar not null
);