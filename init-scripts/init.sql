-- Create Schema
CREATE SCHEMA IF NOT EXISTS saga;

CREATE TABLE IF NOT EXISTS saga.bookings (
    id BIGSERIAL,
    booking_code varchar(50) NOT NULL,
    user_id varchar(255) NOT NULL,
    show_id varchar(255) NOT NULL,
    total_amount numeric(10, 2) NOT NULL,
    status varchar(20) NOT NULL,
    created_at timestamptz(6) NOT NULL,
    updated_at timestamptz(6) NULL,
    "version" int8 NULL,
    CONSTRAINT bookings_pkey PRIMARY KEY (id),
    CONSTRAINT bookings_reservation_code_key UNIQUE (booking_code)
);

CREATE TABLE IF NOT EXISTS saga.seat_inventory (
    id BIGSERIAL,
    theater_id varchar(255) NOT NULL,
    screen_id varchar(255) NOT NULL,
    show_id varchar(255) NOT NULL,
    seat_number varchar(10) NOT NULL,
    booking_code varchar(50) NULL,
    status varchar(20) NOT NULL,
    last_updated timestamptz(6) NULL,
    CONSTRAINT seat_inventory_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS saga.booking_seat (
    booking_id int8 NOT NULL,
    seat_inventory_id varchar(255) NULL,
    CONSTRAINT fk1_bookings_id FOREIGN KEY (booking_id) REFERENCES saga.bookings(id)
);


CREATE TABLE IF NOT EXISTS saga.payment_transaction (
    id BIGSERIAL,
    booking_code varchar(50) NOT NULL,
    total_amount numeric(10, 2) NOT NULL,
    status varchar(20) NOT NULL,
    created_at timestamptz(6) NOT NULL,
    updated_at timestamptz(6) NULL,
    CONSTRAINT payment_transaction_pkey PRIMARY KEY (id),
    CONSTRAINT payment_transaction_booking_code_key UNIQUE (booking_code)
);