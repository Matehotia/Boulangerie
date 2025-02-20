CREATE DATABASE boul;

\c boul;

CREATE TABLE boulangerie_user (
    id_user SERIAL PRIMARY KEY,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    user_password VARCHAR(100) NOT NULL
);
ALTER TABLE boulangerie_user ADD COLUMN role VARCHAR(20) DEFAULT 'CLIENT';
UPDATE boulangerie_user 
SET role = 'ADMIN' 
WHERE email = 'ninahx_x@cook.com';

CREATE TABLE category (
    id_category SERIAL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL
);


CREATE TABLE recipe (
    id_recipe SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    recipe_description TEXT,
    id_category INT NOT NULL,
    cook_time TIME NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_date DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_category) REFERENCES category(id_category)
);


CREATE TABLE ingredient (
    id_ingredient SERIAL PRIMARY KEY,
    ingredient_name VARCHAR(255) NOT NULL,
    unit VARCHAR(50) NOT NULL, -- For example, grams, milliliters, teaspoons, etc.
    price INT NOT NULL DEFAULT 0 
);

CREATE TABLE recipe_ingredient (
    id_recipe INT,
    id_ingredient INT,
    quantity DECIMAL(10,2), -- To store the amount needed for each recipe
    PRIMARY KEY (id_recipe, id_ingredient),
    FOREIGN KEY (id_recipe) REFERENCES recipe(id_recipe),
    FOREIGN KEY (id_ingredient) REFERENCES ingredient(id_ingredient)
);

CREATE TABLE step (
    id_step SERIAL PRIMARY KEY,
    id_recipe INT NOT NULL,
    step_number INT NOT NULL,
    instruction TEXT NOT NULL,
    FOREIGN KEY (id_recipe) REFERENCES recipe(id_recipe)
);

CREATE TABLE review (
    id_review SERIAL PRIMARY KEY,
    id_user INT NOT NULL,
    id_recipe INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    review_date DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_user) REFERENCES boulangerie_user(id_user),
    FOREIGN KEY (id_recipe) REFERENCES recipe(id_recipe)
);

CREATE TABLE vente (
    id_vente SERIAL PRIMARY KEY,
    id_user INT NOT NULL,
    vente_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_user) REFERENCES boulangerie_user(id_user)
);

CREATE TABLE price_history (
    id_price SERIAL PRIMARY KEY,
    id_recipe INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    starts_date DATE NOT NULL DEFAULT CURRENT_DATE,
    end_date DATE,
    modified_by VARCHAR(255) NOT NULL,
    comment TEXT,
    FOREIGN KEY (id_recipe) REFERENCES recipe(id_recipe)
);

CREATE OR REPLACE FUNCTION update_price_history() RETURNS TRIGGER AS $$
BEGIN
    UPDATE price_history 
    SET end_date = CURRENT_DATE - INTERVAL '1 day'
    WHERE id_recipe = NEW.id_recipe 
    AND end_date IS NULL;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_price_insert
    BEFORE INSERT ON price_history
    FOR EACH ROW
    EXECUTE FUNCTION update_price_history();

ALTER TABLE recipe 
DROP COLUMN IF EXISTS price;
DROP COLUMN IF EXISTS status;

-- Vérifier d'abord si la colonne existe
ALTER TABLE price_history 
ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255) NOT NULL DEFAULT 'system';

-- Mettre à jour la table pour les enregistrements existants si nécessaire
UPDATE price_history 
SET modified_by = 'system' 
WHERE modified_by IS NULL;

-- Table pour le stock des ingrédients
CREATE TABLE ingredient_stock (
    id_stock SERIAL PRIMARY KEY,
    id_ingredient INT REFERENCES ingredient(id_ingredient),
    quantity DECIMAL(10,2),
    last_update DATE DEFAULT CURRENT_DATE,
    alert_threshold DECIMAL(10,2),
    modified_by VARCHAR(255)
);

-- Table pour l'historique des mouvements
CREATE TABLE stock_movement (
    id_movement SERIAL PRIMARY KEY,
    id_ingredient INT REFERENCES ingredient(id_ingredient),
    movement_type VARCHAR(20),
    quantity DECIMAL(10,2),
    movement_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reason VARCHAR(255),
    modified_by VARCHAR(255)
);

-- Table pour le stock des recettes (produits finis)
CREATE TABLE recipe_stock (
    id_stock SERIAL PRIMARY KEY,
    id_recipe INT REFERENCES recipe(id_recipe),
    quantity INT NOT NULL DEFAULT 0,
    last_update DATE DEFAULT CURRENT_DATE,
    alert_threshold INT DEFAULT 10,
    modified_by VARCHAR(255)
);

-- Table pour l'historique des mouvements des recettes
CREATE TABLE recipe_stock_movement (
    id_movement SERIAL PRIMARY KEY,
    id_recipe INT REFERENCES recipe(id_recipe),
    movement_type VARCHAR(20), -- 'PRODUCTION' ou 'VENTE'
    quantity INT NOT NULL,
    movement_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reason VARCHAR(255),
    modified_by VARCHAR(255)
);
