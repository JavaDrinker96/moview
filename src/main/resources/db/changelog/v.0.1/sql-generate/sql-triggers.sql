-- 08-13-2022-create-db-triggers
-- author: vladimir_egorov
create or replace function get_avg_movie_score(m_id bigint) returns integer
    language sql
as
$$
SELECT AVG(score)
FROM review as r
WHERE r.movie_id = m_id;
$$;

alter function get_avg_movie_score(bigint) owner to postgres;

create or replace function get_avg_movie_score_for_delete(m_id bigint, deleted_r_id bigint) returns integer
    language sql
as
$$
SELECT AVG(score)
FROM review as r
WHERE r.movie_id = m_id
  AND r.id != deleted_r_id;
$$;

alter function get_avg_movie_score_for_delete(bigint, bigint) owner to postgres;

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