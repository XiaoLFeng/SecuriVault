-- auto-generated definition
create table xf_user
(
    uuid         uuid                    not null
        constraint xf_user_pk
            primary key,
    username     varchar(30)             not null,
    nickname     varchar(30),
    email        varchar                 not null,
    phone        varchar(11)             not null,
    role         uuid
        constraint xf_user_xf_role_ruuid_fk
            references xf_role
            on update cascade on delete set default,
    password     integer,
    old_password integer,
    created_at   timestamp default now() not null,
    updated_at   timestamp               not null
);

comment on table xf_user is '用户表';
comment on column xf_user.uuid is '用户识别码';
comment on column xf_user.username is '用户名';
comment on column xf_user.nickname is '昵称';
comment on column xf_user.email is '邮箱';
comment on column xf_user.phone is '手机';
comment on column xf_user.role is '角色uuid(ruuid)';
comment on constraint xf_user_xf_role_ruuid_fk on xf_user is '角色表 ruuid 外键约束';
comment on column xf_user.created_at is '创建时间';
comment on column xf_user.updated_at is '更新时间';