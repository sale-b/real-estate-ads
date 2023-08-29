CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX filter_ad_type_trgn
    ON filter USING gin(lower(ad_type) gin_trgm_ops);