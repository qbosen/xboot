drop table if exists `content`;
create table `content`
(
    `id`        bigint not null auto_increment,
    `column_id` bigint not null,
    `weight`    bigint,
    primary key (`id`),
    index `name_index` (`column_id`, `weight`)
) engine = innodb
  default charset = utf8mb4;