-- A draft account can be removed. An open account stays on file.
-- Child rows of an account are owned by the account.

ALTER TYPE account_status_enum ADD VALUE IF NOT EXISTS 'DRAFT' BEFORE 'OPEN';
ALTER TYPE status_code_enum ADD VALUE IF NOT EXISTS 'DRAFT' BEFORE 'OPEN';

DO $$
DECLARE
    r RECORD;
    definition TEXT;
BEGIN
    FOR r IN
        SELECT c.conname,
               rel.relname AS table_name,
               pg_get_constraintdef(c.oid) AS definition
        FROM pg_constraint c
        JOIN pg_class rel ON rel.oid = c.conrelid
        JOIN pg_namespace nsp ON nsp.oid = rel.relnamespace
        WHERE c.contype = 'f'
          AND nsp.nspname = current_schema()
          AND pg_get_constraintdef(c.oid) NOT ILIKE '%ON DELETE%'
    LOOP
        definition := r.definition;
        IF definition ILIKE '%DEFERRABLE%' THEN
            definition := regexp_replace(definition, 'DEFERRABLE', 'ON DELETE CASCADE DEFERRABLE');
        ELSE
            definition := definition || ' ON DELETE CASCADE';
        END IF;
        EXECUTE format('ALTER TABLE %I DROP CONSTRAINT %I', r.table_name, r.conname);
        EXECUTE format('ALTER TABLE %I ADD CONSTRAINT %I %s', r.table_name, r.conname, definition);
    END LOOP;
END $$;
