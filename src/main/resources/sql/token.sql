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

-- 用户授权表
create table xf_token
(
    token      varchar(36)             not null
        constraint xf_token_pk
            primary key,
    uuid       varchar(36)             not null
        constraint xf_token_xf_user_uuid_fk
            references xf_user,
    created_at timestamp default now() not null,
    expired_at timestamp               not null
);

comment on table xf_token is '用户授权表';
comment on column xf_token.token is '授权 uuid';
comment on column xf_token.uuid is '用户 uuid';
comment on constraint xf_token_xf_user_uuid_fk on xf_token is '用户 uuid 外键约束';
comment on column xf_token.created_at is '创建时间';
comment on column xf_token.expired_at is '过期时间';
