create sequence global_seq start with 100000 increment by 1;

    create table meals (
       id integer not null,
        calories integer not null check (calories<=5000 AND calories>=10),
        date_time timestamp not null,
        description varchar(120) not null,
        user_id integer not null,
        primary key (id)
    );

    create table user_roles (
       user_id integer not null,
        role varchar(255)
    );

    create table users (
       id integer not null,
        name varchar(100) not null,
        calories_per_day int default 2000 not null check (calories_per_day<=10000 AND calories_per_day>=10),
        email varchar(100) not null,
        enabled bool default true not null,
        password varchar(100) not null,
        registered timestamp default now() not null,
        primary key (id)
    );

    alter table meals 
       add constraint meals_unique_user_datetime_idx unique (user_id, date_time);

    alter table users 
       add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

    alter table meals 
       add constraint FK677c66qpjr7234luomahc1ale 
       foreign key (user_id) 
       references users;

    alter table user_roles 
       add constraint FKhfh9dx7w3ubf1co1vdev94g3f 
       foreign key (user_id) 
       references users;
