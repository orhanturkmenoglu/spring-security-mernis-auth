-- Add email column (unique and not null)
ALTER TABLE users
    ADD COLUMN email VARCHAR(255) NOT NULL UNIQUE;

-- Add created_at column (default current timestamp)
ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Add updated_at column (auto-updates on row update)
ALTER TABLE users
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
