create table if not exists movie
(
    id           serial
        primary key,
    title        text    not null,
    description  text    not null,
    release_date date    not null,
    duration     integer not null,
    rating       integer
);

alter table movie
    owner to postgres;

create table if not exists review
(
    id               serial
        primary key,
    movie_id         integer not null
        constraint fk_movie
            references movie,
    score            integer
        constraint review_score_check
            check ((score > 0) AND (score <= 100)),
    title            text    not null,
    content          text    not null,
    publication_date date    not null
);

alter table review
    owner to postgres;

create or replace function get_avg_movie_score(m_id integer) returns integer
    language sql
as
$$
SELECT AVG(score)
FROM review as r
WHERE r.movie_id = m_id;
$$;

alter function get_avg_movie_score(integer) owner to postgres;

create or replace function get_avg_movie_score_for_delete(m_id integer, deleted_r_id integer) returns integer
    language sql
as
$$
SELECT AVG(score)
FROM review as r
WHERE r.movie_id = m_id
  AND r.id != deleted_r_id;
$$;

alter function get_avg_movie_score_for_delete(integer, integer) owner to postgres;

create or replace function actualize_movie_score() returns trigger
    language plpgsql
as
$$
BEGIN
    UPDATE movie
    SET rating = get_avg_movie_score(new.movie_id)
    WHERE movie.id = new.movie_id;
    RETURN NEW;
END;
$$;

alter function actualize_movie_score() owner to postgres;

create trigger actualize_movie_after_insert_review
    after insert
    on review
    for each row
execute procedure actualize_movie_score();

create trigger actualize_movie_after_update_review
    after update
    on review
    for each row
execute procedure actualize_movie_score();

create or replace function actualize_movie_score_for_delete() returns trigger
    language plpgsql
as
$$
BEGIN
    UPDATE movie
    SET rating = get_avg_movie_score_for_delete(old.movie_id, old.id)
    WHERE movie.id = old.movie_id;
    RETURN OLD;
END;
$$;

alter function actualize_movie_score_for_delete() owner to postgres;

create trigger actualize_movie_before_delete_review
    before delete
    on review
    for each row
execute procedure actualize_movie_score_for_delete();

