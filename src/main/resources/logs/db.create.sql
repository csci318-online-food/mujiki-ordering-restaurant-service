CREATE TYPE IF NOT EXISTS user_roles AS ENUM ('ADMIN', 'USER', 'RESTAURANT');
CREATE TYPE IF NOT EXISTS cuisine_type AS ENUM (
    'CHINESE',
    'JAPANESE',
    'KOREAN',
    'THAI',
    'VIETNAMESE',
    'INDIAN',
    'ITALIAN',
    'MEXICAN',
    'AMERICAN',
    'FRENCH',
    'GREEK',
    'SPANISH',
    'GERMAN',
    'BRAZILIAN',
    'ARGENTINIAN',
    'PERUVIAN',
    'CUBAN',
    'EGYPTIAN',
    'MOROCCAN',
    'TURKISH'
    );


CREATE TABLE IF NOT EXISTS restaurant (
  id UUID PRIMARY KEY,
  name VARCHAR(255),
    description TEXT,
    cuisine cuisine_type,
    open_time TIME,
    close_time TIME,
    country_code VARCHAR(255),
    number VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    rating DOUBLE PRECISION,
    roles user_roles,
    is_opened BOOLEAN DEFAULT FALSE,
    create_at timestamp,
    modify_at timestamp,
    modify_by varchar(64),
    create_by varchar(64)
    );

CREATE TABLE restaurant_event (
  id UUID PRIMARY KEY,
  event_name VARCHAR(255) NOT NULL,
  restaurant_id UUID NOT NULL,
  restaurant_name VARCHAR(255) NOT NULL,
  details TEXT,
  country_code VARCHAR(255),
  number VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS promotion (
    id UUID PRIMARY KEY,
    restaurant_id UUID,
    discount_code VARCHAR(255),
    description TEXT,
    expiry_date DATE,
    is_active BOOLEAN DEFAULT FALSE,
    percentage INT,
    create_at timestamp,
    stock INT
)