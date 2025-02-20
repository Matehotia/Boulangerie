
CREATE TABLE price_history (
    id_price SERIAL PRIMARY KEY,               -- Identifiant unique pour chaque variation de prix
    id_recipe INT NOT NULL,                    -- Identifiant de la recette
    price DECIMAL(10, 2) NOT NULL,             -- Prix de la recette
    starts_date DATE NOT NULL DEFAULT CURRENT_DATE, -- Date de début de validité
    end_date DATE                              -- Date de fin de validité (NULL si encore actif)
);
-- Vérifier d'abord si la colonne existe
ALTER TABLE price_history 
ADD COLUMN IF NOT EXISTS modified_by VARCHAR(255) NOT NULL DEFAULT 'system';

-- Mettre à jour la table pour les enregistrements existants si nécessaire
UPDATE price_history 
SET modified_by = 'system' 
WHERE modified_by IS NULL;

ALTER TABLE price_history 
ADD COLUMN IF NOT EXISTS comment TEXT;


-- Insérer les prix initiaux pour chaque recette
INSERT INTO price_history (id_recipe, price, starts_date, end_date)
VALUES
    (1, 3.50, '2025-01-05', NULL), -- Pain au Beurre
    (2, 4.00, '2025-01-05', NULL), -- Chocolate Croissant
    (3, 5.50, '2025-01-05', NULL), -- Lemon Cake
    (4, 3.75, '2025-01-06', NULL), -- Croissant au Beurre
    (5, 2.25, '2025-01-06', NULL); -- Madeleine


CREATE OR REPLACE VIEW get_actual_price AS
SELECT 
    r.id_recipe,
    r.title,
    r.recipe_description,
    r.id_category,
    r.cook_time,
    r.created_by,
    r.created_date,
    p.price AS actual_price
FROM 
    recipe r
JOIN 
    price_history p
ON 
    r.id_recipe = p.id_recipe
WHERE 
    p.end_date IS NULL;


SELECT * FROM get_actual_price;



CREATE VIEW ingredient_parfum AS 
SELECT * 
FROM ingredient 
WHERE is_nature = FALSE;

-- Mettre à jour tous les anciens prix pour qu'ils aient une date de fin
WITH RankedPrices AS (
  SELECT 
    id_price,
    id_recipe,
    starts_date,
    LEAD(starts_date) OVER (PARTITION BY id_recipe ORDER BY starts_date) as next_start_date
  FROM price_history
)
UPDATE price_history ph
SET end_date = rp.next_start_date - INTERVAL '1 day'
FROM RankedPrices rp
WHERE ph.id_price = rp.id_price
AND rp.next_start_date IS NOT NULL;

-- Garder seulement le dernier prix comme actif (end_date NULL)
WITH LastPrices AS (
  SELECT id_recipe, MAX(starts_date) as max_date
  FROM price_history
  GROUP BY id_recipe
)
UPDATE price_history ph
SET end_date = NULL
FROM LastPrices lp
WHERE ph.id_recipe = lp.id_recipe
AND ph.starts_date = lp.max_date;