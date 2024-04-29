-- 密码库
create table xf_password_library
(
    id       uuid    not null
        constraint xf_password_library_pk
            primary key,
    uuid     uuid    not null
        constraint xf_password_library_xf_user_uuid_fk
            references xf_user
            on update cascade on delete cascade,
    website  varchar not null,
    username varchar not null,
    password varchar,
    other    json
);

comment on table xf_password_library is '密码库';
comment on column xf_password_library.id is '密码库';
comment on column xf_password_library.uuid is '所熟人';
comment on constraint xf_password_library_xf_user_uuid_fk on xf_password_library is '用户 uuid 外键约束';
comment on column xf_password_library.website is '网址域名';
comment on column xf_password_library.username is '对应网站用户名';
comment on column xf_password_library.password is '对应网站密码';
comment on column xf_password_library.other is '其他键值对的匹配';
