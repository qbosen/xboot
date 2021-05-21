drop table if exists `content_base`;
create table `content_base`
(
    `id`        bigint not null auto_increment,
    `column_id` bigint not null,
    `weight`    bigint,
    primary key (`id`),
    index `content__column_weight_index` (`column_id`, `weight`)
) engine = innodb
  default charset = utf8mb4;

drop table if exists `content_stick`;
create table `content_stick`
(
    `id`        bigint not null auto_increment,
    `column_id` bigint not null,
    `stick`     bit    not null default 0,
    `weight`    bigint,
    primary key (`id`),
    index `content_stick__column_weight_index` (`column_id`, `weight`),
    index `content_stick__stick` (`stick`)
) engine = innodb
  default charset = utf8mb4;

