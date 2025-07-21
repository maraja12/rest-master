create table invoice(
    id bigint not null,
    company_pib int not null,
    issue_date date not null,
    constraint ck_issue_date check ( issue_date < GETDATE() ),
    payment_date date,
    constraint ck_payment_date check ( payment_date is null or payment_date >= issue_date ),
    status nvarchar(10) not null,
    constraint ck_status check (status in ('NAPLAĆENO', 'NENAPLAĆENO')),
    total_price decimal(18,2) not null,
    primary key (id, company_pib),
    constraint company_fk foreign key (company_pib) references company(pib) on delete cascade
)