--funkce asciize umozni vyhledavat bez diakritiky ve sloupcich, ktere diakritiku obsahuji
--musi se spustit rucne proti databazi
DROP FUNCTION IF EXISTS asciize(str text);
DROP FUNCTION IF EXISTS to_ascii(bytea, name);

CREATE FUNCTION to_ascii(bytea, name)
  RETURNS text AS 'to_ascii_encname'
LANGUAGE internal;

CREATE FUNCTION asciize(str text)
  RETURNS text
LANGUAGE plpgsql IMMUTABLE
AS $$
BEGIN
  str := lower(public.to_ascii(convert_to(str, 'LATIN2'), 'LATIN2'));
  str := regexp_replace(str, '[^a-z0-9.,();%]', ' ', 'g');
  str := regexp_replace(str, '\s\s+', ' ', 'g');
  str := replace(trim(str), ' ', '-');
  RETURN str;
END;
$$;