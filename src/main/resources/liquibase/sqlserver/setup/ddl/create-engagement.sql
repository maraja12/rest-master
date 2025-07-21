create table engagement(
    project_id bigint not null,
    employee_id bigint not null,
    month varchar(10) not null,
    constraint ck_month check (month in ('JANUAR', 'FEBRUAR', 'MART', 'APRIL', 'MAJ', 'JUN', 'JUL', 'AVGUST',
    'SEPTEMBAR', 'OKTOBAR', 'NOVEMBAR', 'DECEMBAR')),
    year int not null,
    constraint ck_year_limit check ( year > 2020 ),
    role nvarchar(30) not null,
    constraint ck_role check (role in ('DEVELOPER', 'TESTER', 'UI_DIZAJNER', 'DATA_ANALYST',
    'FINANSIJSKI_DIREKTOR', 'MENADŽER_PROJEKTA', 'SCRUM_MASTER')),
    num_of_hours int not null,
    constraint ck_hours_limit check (num_of_hours > 0),
    primary key (project_id, employee_id, month, year),
    constraint project_fk foreign key (project_id) references project(id),
    constraint employee_fk foreign key (employee_id) references employee(id)
)