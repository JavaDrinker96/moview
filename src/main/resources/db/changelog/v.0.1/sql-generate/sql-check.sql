-- 08-15-2022-create-check-constraint-for-reviews-score
-- author: vladimir_egorov
ALTER TABLE review ADD CONSTRAINT check_score CHECK ( score >= 1 AND score <= 100);