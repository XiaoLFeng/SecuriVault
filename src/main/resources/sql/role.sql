-- 角色表
create table xf_role
(
    ruuid        uuid                    not null
        constraint xf_role_pk
            primary key,
    name         varchar(30)             not null,
    display_name varchar(30),
    description  varchar(255),
    created_at   timestamp default now() not null,
    updated_at   timestamp
);

comment on table xf_role is '角色表';
comment on column xf_role.ruuid is '角色 uuid';
comment on column xf_role.name is '角色名';
comment on column xf_role.display_name is '角色展示名';
comment on column xf_role.description is '展示名';
comment on column xf_role.created_at is '创建时间';
comment on column xf_role.updated_at is '更新时间';

create unique index xf_role_name_uindex
    on xf_role (name);

