create table scaffold.project
(
    project_code varchar(255) not null
        primary key,
    project_type varchar(255) not null
);


create table scaffold.project_member
(
    id           int auto_increment
        primary key,
    user_id      varchar(255) not null,
    project_code varchar(255) not null,
    role_name    varchar(255) not null
);


