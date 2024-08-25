create table comments (
    id bigserial,
    book_id bigint references books(id),
    comment_text varchar(255),
    primary key (id)
);