DROP INDEX IF EXISTS idx_delivery_person_email;

ALTER TABLE delivery_person RENAME COLUMN email TO phone;

CREATE INDEX idx_delivery_person_phone ON delivery_person(phone);
