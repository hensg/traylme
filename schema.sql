create database traylme;

use traylme;

create table shorted_path_char_sequence (
    shard_id int not null primary key,
    last_char_sequence varchar(100) not null,
    version long
)
engine = innodb
;

create table anonymous_user (
    id bigint not null auto_increment,
    cookie_id varchar(255) not null,
    created_at datetime not null,
    last_updated_at datetime not null,
    locale char(5),
    primary key (id)
)
engine = innodb
partition by key(id)
partitions 6
;

create table traceable_url (
    id bigint not null auto_increment,
    shorted_path varchar(100) not null,
    original_url varchar(2091) not null,
    created_at date not null,
    anonymous_user_id bigint not null,
    primary key (id)
    -- foreign key (anonymous_user_id) references anonymous_user(id),
    -- unique key(shorted_path)
)
engine = innodb
partition by key(id)
partitions 12
;

create index traceable_url_shorted_path_idx on traceable_url(shorted_path);

create table url_recent_hits (
    traceable_url_id bigint not null,
    access_date date not null,
    counter bigint not null,
    primary key (traceable_url_id, access_date)
)
engine = innodb
partition by key(access_date)
partitions 30
;
--todo: auto delete url_recent_hits where access_date > now + 29d