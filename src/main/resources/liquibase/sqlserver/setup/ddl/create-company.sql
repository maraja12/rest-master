create table company(
    pib int not null,
    constraint ck_pib_limit check ( pib between 100000000 and 999999999),
    name nvarchar(30) collate Latin1_General_CS_AS not null,
    constraint ck_capitalized check (name LIKE '[A-Ž]%'),
    address nvarchar(50) not null,
    email varchar(30) not null unique,
    constraint ck_email_formatting check ( email like '%_@_%._%' ),
    central_office bigint not null,
    primary key (pib),
    constraint fk_place foreign key(central_office) references place(zip_code)
)