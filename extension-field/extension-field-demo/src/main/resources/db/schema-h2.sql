create table `content`
(
    id           bigint primary key auto_increment,
    content_type varchar(50)   not null,
    title        varchar(50),
    body         text,
    extension    text not null
);

create table `content_type`
(
    `key`  varchar(50) primary key,
    name   varchar(50)   not null,
    title  varchar(50),
    fields text not null
);

