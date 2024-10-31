create table employee(
    id bigint identity not null,
    name nvarchar(30) not null,
    constraint ck_name_capitalized check ( name like '[A-Ž]%' ),
    surname nvarchar(30) not null,
    constraint ck_surname_capitalized check ( name like '[A-Ž]%' ),
    birthday date not null,
    address nvarchar(50) not null,
    email varchar(30) not null unique,
    constraint ck_email_format check ( email like '%_@_%._%' ),
    profession_id bigint not null,
    primary key (id),
    constraint profession_fk foreign key (profession_id) references profession(id)
)