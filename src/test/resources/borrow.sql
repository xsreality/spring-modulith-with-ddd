INSERT INTO borrow_books (id, version, title, barcode, isbn, status)
VALUES (3999, 0, 'Sapiens', '13268510', '9780062316097', 'AVAILABLE'),
       (3998, 0, 'To Kill a Mockingbird', '49031878', '9780446310789', 'ON_HOLD'),
       (3997, 0, '1984', '37040952', '9780451520500', 'ISSUED');

INSERT INTO loan (id, version, book_id, patron_id, date_of_hold, date_of_checkout,
                  hold_duration_in_days,
                  loan_duration_in_days, date_of_checkin, status)
VALUES (10, 0, 3998, 800, '2023-03-11', NULL, 3, 14, NULL, 'HOLDING'),
       (11, 0, 3997, 800, '2023-03-24', '2023-03-25', 3, 14, NULL, 'ACTIVE');

INSERT INTO patron (id, status)
VALUES (800, 'ACTIVE');
