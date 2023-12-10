INSERT INTO book (id, title, barcode, isbn, author, status)
VALUES
    (1, 'Sapiens', '13268510', '9780062316097', 'Yuval Noah Harari', 'AVAILABLE'),
    (2, 'To Kill a Mockingbird', '49031878', '9780446310789', 'Harper Lee', 'ISSUED');

INSERT INTO loan(id, book_barcode, patron_id, date_of_issue, loan_duration_in_days, date_of_return, status)
VALUES (10, '49031878', NULL, '2023-03-11', 14, NULL, 'ACTIVE');
