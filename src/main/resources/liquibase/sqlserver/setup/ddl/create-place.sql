CREATE TABLE place (
    zip_code bigint not null,
    constraint zip_code_limit check (zip_code between 11000 and 50000),
    name nvarchar(50) not null,
    primary key (zip_code)
)