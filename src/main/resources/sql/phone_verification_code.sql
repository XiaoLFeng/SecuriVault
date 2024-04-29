-- 手机验证码
create table xf_phone_verification_code
(
    id         bigserial
        constraint xf_email_verification_code_pk
            primary key,
    phone      varchar(11)             not null,
    code       varchar(10)             not null,
    created_at timestamp default now() not null,
    expired_at timestamp               not null
);

comment on table xf_phone_verification_code is '邮箱验证码';
comment on column xf_phone_verification_code.id is '自增主键';
comment on column xf_phone_verification_code.phone is '邮箱';
comment on column xf_phone_verification_code.code is '验证码';
comment on column xf_phone_verification_code.created_at is '创建时间';
comment on column xf_phone_verification_code.expired_at is '过期时间';

