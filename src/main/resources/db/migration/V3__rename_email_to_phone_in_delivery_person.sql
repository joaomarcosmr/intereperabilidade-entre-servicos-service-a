-- Drop old index on email column
DROP INDEX IF EXISTS idx_delivery_person_email;

-- Rename email column to phone in delivery_person table
ALTER TABLE delivery_person RENAME COLUMN email TO phone;

-- Create index on phone column
CREATE INDEX idx_delivery_person_phone ON delivery_person(phone);
