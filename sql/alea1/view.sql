--pour tester
SELECT 
    r.*
FROM 
    recipe r
JOIN 
    recipe_ingredient ri ON r.id_recipe = ri.id_recipe
WHERE 
    ri.id_ingredient = 1; -- Remplacez `1` par l'ID de l'ingrédient recherché


INSERT INTO recipe (id_recipe, title, recipe_description, id_category, cook_time, created_by, created_date) VALUES
(4, 'Croissant au Beurre', 'A classic French croissant with a buttery, flaky texture.', 3, '01:00:00', 'Jean Dupont', '2025-01-06'),
(5, 'Madeleine', 'A light, small cake with a distinctive shell-like shape and a hint of lemon.', 2, '00:30:00', 'Sophie Renard', '2025-01-06');



INSERT INTO ingredient (id_ingredient, ingredient_name, unit, price) VALUES
(8, 'Chocolate', 'grams', 1200),  -- New ingredient for Chocolate Croissant
(9, 'Milk', 'milliliters', 800);  -- New ingredient for Croissant au Beurre


INSERT INTO recipe_ingredient (, id_ingredient, quantity) VALUES
(4, 1, 400.00),  -- Flour for the Butter Croissant
(4, 2, 5id_recipe0.00),   -- Sugar for the Butter Croissant
(4, 3, 150.00),  -- Butter for the Butter Croissant
(5, 1, 300.00),  -- Flour for the Madeleine
(5, 2, 100.00),  -- Sugar for the Madeleine
(5, 3, 100.00),  -- Butter for the Madeleine
(5, 4, 2.00),    -- Eggs for the Madeleine
(5, 7, 5.00);    -- Salt for the Madeleine


INSERT INTO recipe_ingredient (id_recipe, id_ingredient, quantity) VALUES
(1, 3, 1000.00),
(2, 7, 1000.00);
