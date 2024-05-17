/*
 * *******************************************************************************
 * Copyright (c) 2016-NOW(至今) 筱锋
 * Author: 筱锋(https://www.x-lf.com)
 *
 * 本文件包含 SecuriValue 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。
 * 本代码仅进行 Java 大作业提交，个人发行版本计划使用 Go 语言重构。
 * *******************************************************************************
 * 许可证声明：
 *
 * 版权所有 (c) 2016-2024 筱锋。保留所有权利。
 *
 * 本软件是“按原样”提供的，没有任何形式的明示或暗示的保证，包括但不限于
 * 对适销性、特定用途的适用性和非侵权性的暗示保证。在任何情况下，
 * 作者或版权持有人均不承担因软件或软件的使用或其他交易而产生的、
 * 由此引起的或以任何方式与此软件有关的任何索赔、损害或其他责任。
 *
 * 由于作者需要进行 Java 大作业提交，所以请勿抄袭。您可以作为参考，但是
 * 一定不可以抄袭，尤其是同校同学！！！
 * 你们可以自己参考代码优化给你们提供思路，开源目的不是给你们抄袭的，共
 * 同维护好开源的社区环境！！！
 *
 * 使用本软件即表示您了解此声明并同意其条款。
 *
 * 有关MIT许可证的更多信息，请查看项目根目录下的LICENSE文件或访问：
 * https://opensource.org/licenses/MIT
 * *******************************************************************************
 * 免责声明：
 *
 * 使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内，
 * 对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * *******************************************************************************
 */

-- 用户表
create table xf_user
(
    uuid         varchar(36)             not null constraint xf_user_pk
            primary key,
    username     varchar(30)             not null,
    nickname     varchar(30),
    email        varchar                 not null,
    phone        varchar(11)             not null,
    role         varchar(36) constraint xf_user_xf_role_ruuid_fk
            references xf_role
            on update cascade on delete set default,
    password     varchar                 not null,
    old_password varchar,
    enabled      boolean   default true  not null,
    banned       boolean   default false not null,
    created_at   timestamp default now() not null,
    updated_at   timestamp
);

comment on table xf_user is '用户表';
comment on column xf_user.uuid is '用户识别码';
comment on column xf_user.username is '用户名';
comment on column xf_user.nickname is '昵称';
comment on column xf_user.email is '邮箱';
comment on column xf_user.phone is '手机';
comment on column xf_user.role is '角色uuid(ruuid)';
comment on constraint xf_user_xf_role_ruuid_fk on xf_user is '角色表 ruuid 外键约束';
comment on column xf_user.password is '密码';
comment on column xf_user.old_password is '旧密码';
comment on column xf_user.enabled is '是否启用';
comment on column xf_user.banned is '是否封禁';
comment on column xf_user.created_at is '创建时间';
comment on column xf_user.updated_at is '更新时间';
