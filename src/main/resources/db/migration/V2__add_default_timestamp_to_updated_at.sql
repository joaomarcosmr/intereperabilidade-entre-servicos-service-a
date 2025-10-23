-- Update existing NULL values to current timestamp FIRST
UPDATE users SET updated_at = created_at WHERE updated_at IS NULL;
UPDATE delivery_person SET updated_at = created_at WHERE updated_at IS NULL;
UPDATE orders SET updated_at = created_at WHERE updated_at IS NULL;

-- Add DEFAULT CURRENT_TIMESTAMP to updated_at columns in all tables
ALTER TABLE users
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE delivery_person
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET NOT NULL;

ALTER TABLE orders
    ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP,
    ALTER COLUMN updated_at SET NOT NULL;
