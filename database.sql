drop table if exists orders;
drop table if exists clients;


create table clients (
    id serial primary key,
    full_name varchar(50) not null,
    phone varchar(15) not null unique,
    email varchar(50) unique
);

create table orders (
    id serial primary key,
    car_brand varchar(40) not null,
    car_number varchar(10) not null,
    service_type varchar(20) not null,

    status varchar(20) not null
        check( status IN ('NEW', 'IN_PROGRESS', 'DONE', 'CANCELLED')),

    price numeric(10,2) not null 
        check(price >= 0),

    create_date timestamp not null default now(),
    client_id int not null,
        constraint fk_order_client
        foreign key (client_id) references clients(id)
        on delete cascade
);

insert into clients (full_name, phone, email) values
('Иванов Иван Иванович', '+79001112233', 'ivanov@mail.ru'),
('Петров Петр Петрович', '+79002223344', 'petrov@mail.ru'),
('Сидорова Анна Сергеевна', '+79003334455', 'sidorova@mail.ru'),
('Кузнецов Олег Андреевич', '+79004445566', 'kuznetsov@mail.ru'),
('Смирнова Ольга Дмитриевна', '+79005556677', 'smirnova@mail.ru');

insert into orders(car_brand, car_number, service_type, status , price, client_id) values
('Toyota Camry', 'А001МР77', 'Замена шин', 'NEW', 4500, 1),
('Kia Rio', 'С159КР97', 'Балансировка', 'IN_PROGRESS', 2500, 2),
('BMW X5', 'М666УР97', 'Ремонт прокола', 'NEW', 1800, 3),
('Lada Vesta', 'Е012КР77', 'Сезонная замена', 'DONE', 5200, 1),
('AUDI A6', 'М111УР97', 'Подкачка', 'NEW', 800, 4),
('AUDI A4', 'С275ХС797', 'Балансировка ', 'IN_PROGRESS', 4000, 2),
('Mazda 6', 'У901УР777', 'Замена шин', 'NEW', 4800, 5),
('Nissan Almera', 'Н656ТХ777', 'Балансировка', 'CANCELLED', 2200, 3),
('Volkswagen Golf GTI', 'А123УЕ199', 'Ремонт прокола', 'DONE', 1500, 4),
('Skoda Octavia', 'М020УР97', 'Сезонная замена', 'NEW', 5100, 5);