create table profession (
    id bigint identity not null,
    name nvarchar(30) not null unique,
    primary key (id)
)