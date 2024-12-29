create table invoice_item(
    seq_no bigint not null,
    invoice_id bigint not null,
    invoice_company_pib int not null,
    description varchar(100),
    price_per_hour decimal(18, 2) not null,
    project_id bigint not null,
    employee_id bigint not null,
    month varchar(10) not null,
    year int not null,
    constraint ck_price_limit check ( price_per_hour > 0 ),
    primary key (seq_no, invoice_id, invoice_company_pib),
    constraint invoice_fk foreign key (invoice_id, invoice_company_pib) references invoice(id, company_pib) on delete cascade,
    constraint engagement_fk foreign key (project_id, employee_id, month, year)
    references engagement(project_id, employee_id, month, year) on delete cascade
)
