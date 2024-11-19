create table person
(
    id         serial8      not null
        constraint person_pkey
            primary key,
    name       varchar(100) not null,
    dob        date,
    status     varchar(25)  not null,
    created_at timestamptz default CURRENT_TIMESTAMP,
    updated_at timestamptz default CURRENT_TIMESTAMP
);

-- There are some records
-- INSERT INTO public."person" (id, name, dob, status, created_at, updated_at) VALUES (1, 'Dam Trinh Hung', '2000-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00');
-- INSERT INTO public."person" (id, name, dob, status, created_at, updated_at) VALUES (2, 'Nguyen Van An', '2001-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00');
-- INSERT INTO public."person" (id, name, dob, status, created_at, updated_at) VALUES (3, 'Luong Thi Hien', '2002-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00');
-- INSERT INTO public."person" (id, name, dob, status, created_at, updated_at) VALUES (4, 'Truong Huyen Anh', '1999-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00');
-- INSERT INTO public."person" (id, name, dob, status, created_at, updated_at) VALUES (5, 'Vu Van Thieu', '1998-02-03', 'ACTIVATE', '2023-12-13 08:02:02.635393 +00:00', '2023-12-13 08:02:02.635393 +00:00');
-- INSERT INTO public."person" (id, name, dob, status, created_at, updated_at) VALUES (6, 'Nguyen Thi Nhung', '1998-02-03', 'PROCESSING', '2023-12-13 08:03:31.815812 +00:00', '2023-12-13 08:03:31.815812 +00:00');
-- INSERT INTO public."person" (id, name, dob, status, created_at, updated_at) VALUES (7, 'Nguyen Duc Tung 1', null, 'ACTIVATE', '2023-12-13 09:33:04.440109 +00:00', '2023-12-13 18:44:28.432904 +00:00');
-- INSERT INTO public."person" (id, name, dob, status, created_at, updated_at) VALUES (8, 'Nguyen Thu Huong 1', null, 'ACTIVATE', '2023-12-13 09:33:04.440109 +00:00', '2023-12-13 18:44:28.432904 +00:00');
-- INSERT INTO public."person" (id, name, dob, status, created_at, updated_at) VALUES (13, 'Huynh Duong Gia', null, 'ACTIVATE', '2023-12-13 18:23:58.553652 +00:00', '2023-12-13 18:23:58.553652 +00:00');
