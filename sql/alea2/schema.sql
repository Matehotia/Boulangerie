CREATE TABLE vente (
    id_vente SERIAL PRIMARY KEY,        -- Identifiant unique de la vente
    id_user INT,                        -- Identifiant de l'utilisateur (client) si connecté
    vente_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Date et heure de la vente
    total_amount DECIMAL(10, 2) NOT NULL DEFAULT 0, -- Montant total de la vente
    FOREIGN KEY (id_user) REFERENCES boulangerie_user(id_user) -- Lien avec l'utilisateur (facultatif)
);


CREATE TABLE vente_details (
    id_vente INT NOT NULL,              -- Identifiant de la vente (clé étrangère)
    id_recipe INT NOT NULL,             -- Identifiant de la recette vendue (clé étrangère)
    quantity INT NOT NULL CHECK (quantity > 0), -- Quantité vendue
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0), -- Prix unitaire
    PRIMARY KEY (id_vente, id_recipe),  -- Clé primaire composite
    FOREIGN KEY (id_vente) REFERENCES vente(id_vente) ON DELETE CASCADE, -- Suppression en cascade
    FOREIGN KEY (id_recipe) REFERENCES recipe(id_recipe) ON DELETE CASCADE -- Suppression en cascade
);



INSERT INTO vente (id_vente,id_user, vente_date, total_amount)
VALUES 
    (1,1, '2025-01-07 10:30:00', 45.00), -- Vente 1
    (2,2, '2025-01-07 14:00:00', 60.50), -- Vente 2
    (3,3, '2025-01-08 09:15:00', 25.00); -- Vente 3 (Utilisateur non connecté)


-- Détails pour la vente 1
INSERT INTO vente_details (id_vente, id_recipe, quantity, unit_price)
VALUES 
    (1, 2, 2, 12.50), -- 2 Chocolate Croissants à 12.50 chacun
    (1, 3, 1, 20.00); -- 1 Lemon Cake à 20.00

-- Détails pour la vente 2
INSERT INTO vente_details (id_vente, id_recipe, quantity, unit_price)
VALUES 
    (2, 4, 3, 10.50), -- 3 Croissants au Beurre à 10.50 chacun
    (2, 5, 2, 12.50); -- 2 Madeleines à 12.50 chacune

-- Détails pour la vente 3
INSERT INTO vente_details (id_vente, id_recipe, quantity, unit_price)
VALUES 
    (3, 1, 1, 25.00); -- 1 Pain au Beurre à 25.00



CREATE VIEW liste_ventes AS
SELECT 
    v.id_vente,
    v.vente_date,
    u.firstname || ' ' || u.lastname AS user_name,
    vd.id_recipe,
    r.title AS recipe_name,
    vd.quantity AS qtt,
    vd.unit_price AS pu,
    (vd.quantity * vd.unit_price) AS sub_total,
    v.total_amount AS total_vente
FROM 
    vente v
LEFT JOIN 
    boulangerie_user u ON v.id_user = u.id_user
LEFT JOIN 
    vente_details vd ON v.id_vente = vd.id_vente
LEFT JOIN 
    recipe r ON vd.id_recipe = r.id_recipe;



ALTER TABLE ingredient ADD COLUMN is_nature BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE ingredient
SET is_nature = FALSE
WHERE ingredient_name IN ('Chocolate', 'Vanilla Extract');


CREATE OR REPLACE VIEW recipe_nature AS
SELECT r.id_recipe,
       r.title,
       CASE 
           WHEN EXISTS (
               SELECT 1
               FROM recipe_ingredient ri
               JOIN ingredient i ON ri.id_ingredient = i.id_ingredient
               WHERE ri.id_recipe = r.id_recipe
                 AND i.is_nature = 'f'
           ) THEN FALSE  -- If any ingredient is not nature, mark the recipe as non-nature
           ELSE TRUE  -- If all ingredients are nature, mark the recipe as nature
       END AS is_nature
FROM recipe r;




SELECT * FROM recipe_nature;


CREATE VIEW vente_filtre AS
SELECT 
    v.id_vente,
    v.vente_date,
    u.firstname || ' ' || u.lastname AS user_name,
    r.title AS recipe,
    c.category_name,
    vn.is_nature,
    vd.quantity,
    vd.unit_price,
    (vd.quantity * vd.unit_price) AS sub_total,
    v.total_amount
FROM 
    vente v
LEFT JOIN 
    boulangerie_user u ON v.id_user = u.id_user
JOIN 
    vente_details vd ON v.id_vente = vd.id_vente
JOIN 
    recipe r ON vd.id_recipe = r.id_recipe
JOIN 
    category c ON r.id_category = c.id_category
JOIN 
    recipe_nature vn ON r.id_recipe = vn.id_recipe;


SELECT * 
FROM vente_filtre
WHERE category_name = 'Pastries' AND is_nature = TRUE;
    
INSERT INTO vente (id_user, vente_date, total_amount)
VALUES 
    (1, '2025-01-07 10:30:00', 45.00), -- Vente 1
    (2, '2025-01-07 14:00:00', 60.50), -- Vente 2
    (NULL, '2025-01-08 09:15:00', 25.00); -- Vente 3 (Utilisateur non connecté)


-- Détails pour la vente 1
INSERT INTO vente_details (id_vente, id_recipe, quantity, unit_price)
VALUES 
    (1, 2, 2, 12.50), -- 2 Chocolate Croissants à 12.50 chacun
    (1, 3, 1, 20.00); -- 1 Lemon Cake à 20.00

-- Détails pour la vente 2
INSERT INTO vente_details (id_vente, id_recipe, quantity, unit_price)
VALUES 
    (2, 4, 3, 10.50), -- 3 Croissants au Beurre à 10.50 chacun
    (2, 5, 2, 12.50); -- 2 Madeleines à 12.50 chacune

-- Détails pour la vente 3
INSERT INTO vente_details (id_vente, id_recipe, quantity, unit_price)
VALUES 
    (3, 1, 1, 25.00); -- 1 Pain au Beurre à 25.00



CREATE OR REPLACE VIEW liste_ventes AS
SELECT 
    v.id_vente,
    v.vente_date,
    u.firstname || ' ' || u.lastname AS user_name,
    vd.id_recipe,
    r.title AS recipe_name,
    vd.quantity AS qtt,
    vd.unit_price AS pu,
    (vd.quantity * vd.unit_price) AS sub_total,
    v.total_amount AS total_vente
FROM 
    vente v
LEFT JOIN 
    boulangerie_user u ON v.id_user = u.id_user
LEFT JOIN 
    vente_details vd ON v.id_vente = vd.id_vente
LEFT JOIN 
    recipe r ON vd.id_recipe = r.id_recipe;



ALTER TABLE ingredient ADD COLUMN is_nature BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE ingredient
SET is_nature = FALSE
WHERE ingredient_name IN ('Chocolate', 'Vanilla Extract');


CREATE OR REPLACE VIEW recipe_nature AS
SELECT r.id_recipe,
       r.title,
       CASE 
           WHEN EXISTS (
               SELECT 1
               FROM recipe_ingredient ri
               JOIN ingredient i ON ri.id_ingredient = i.id_ingredient
               WHERE ri.id_recipe = r.id_recipe
                 AND i.is_nature = 'f'
           ) THEN FALSE  -- If any ingredient is not nature, mark the recipe as non-nature
           ELSE TRUE  -- If all ingredients are nature, mark the recipe as nature
       END AS is_nature
FROM recipe r;




SELECT * FROM recipe_nature;


CREATE OR REPLACE VIEW vente_filtre AS
SELECT 
    v.id_vente,
    v.vente_date,
    u.firstname || ' ' || u.lastname AS user_name,
    r.title AS recipe,
    c.category_name,
    vn.is_nature,
    vd.quantity,
    vd.unit_price,
    (vd.quantity * vd.unit_price) AS sub_total,
    v.total_amount
FROM 
    vente v
LEFT JOIN 
    boulangerie_user u ON v.id_user = u.id_user
JOIN 
    vente_details vd ON v.id_vente = vd.id_vente
JOIN 
    recipe r ON vd.id_recipe = r.id_recipe
JOIN 
    category c ON r.id_category = c.id_category
JOIN 
    recipe_nature vn ON r.id_recipe = vn.id_recipe;


SELECT * 
FROM vente_filtre
WHERE category_name = 'Pastries' AND is_nature = TRUE;


INSERT INTO ingredient_stock (id_ingredient, quantity, alert_threshold, modified_by) 
VALUES 
    (1, 1000.00, 100.00, 'Admin'), -- Farine
    (2, 500.00, 50.00, 'Admin'),   -- Sucre
    (3, 250.00, 25.00, 'Admin'),   -- Beurre
    (4, 300.00, 30.00, 'Admin'),   -- Oeufs
    (5, 150.00, 20.00, 'Admin'),   -- Chocolat
    (6, 200.00, 25.00, 'Admin'),   -- Levure
    (7, 100.00, 15.00, 'Admin');   -- Sel









-- D'abord, vérifions les ingrédients pour toutes les recettes
SELECT r.title, i.ingredient_name, ri.quantity
FROM recipe r
JOIN recipe_ingredient ri ON r.id_recipe = ri.id_recipe
JOIN ingredient i ON ri.id_ingredient = i.id_ingredient
ORDER BY r.title;

-- Mettons à jour les stocks avec des quantités très importantes
UPDATE ingredient_stock
SET quantity = CASE 
    WHEN id_ingredient = 1 THEN 100000.00  -- Flour (pour 200 baguettes, 500 croissants, 333 gâteaux)
    WHEN id_ingredient = 2 THEN 50000.00   -- Sugar (pour 5000 baguettes, 500 croissants, 333 gâteaux)
    WHEN id_ingredient = 3 THEN 200000.00  -- Butter (pour 200 baguettes, 4000 croissants)
    WHEN id_ingredient = 4 THEN 3000.00    -- Eggs (pour 1000 gâteaux)
    WHEN id_ingredient = 5 THEN 10000.00   -- Chocolate (pour les croissants)
    WHEN id_ingredient = 6 THEN 5000.00    -- Yeast (pour 1000 baguettes)
    WHEN id_ingredient = 7 THEN 200000.00  -- Salt (pour 200 croissants, 100000 gâteaux)
    ELSE quantity
END;

-- Vérifions le stock des ingrédients après la mise à jour
SELECT i.ingredient_name, s.quantity, s.alert_threshold
FROM ingredient_stock s
JOIN ingredient i ON s.id_ingredient = i.id_ingredient
ORDER BY i.id_ingredient;

-- Vérifions aussi le stock des produits finis
SELECT r.title, rs.quantity, rs.alert_threshold
FROM recipe_stock rs
JOIN recipe r ON rs.id_recipe = r.id_recipe;

-- Mettons à jour les stocks de produits finis avec des quantités plus importantes
UPDATE recipe_stock 
SET quantity = CASE 
    WHEN id_recipe = 1 THEN 100  -- Baguette
    WHEN id_recipe = 2 THEN 80   -- Chocolate Croissant
    WHEN id_recipe = 3 THEN 60   -- Lemon Cake
    ELSE quantity
END,
modified_by = 'Admin',
last_update = CURRENT_DATE;

-- Vérifions le nouveau stock des produits finis
SELECT r.title, rs.quantity, rs.alert_threshold
FROM recipe_stock rs
JOIN recipe r ON rs.id_recipe = r.id_recipe
ORDER BY r.title;
