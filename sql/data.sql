-- Insert into the 'category' table
INSERT INTO category (category_name) VALUES
    ('Breads'), 
    ('Cakes'), 
    ('Pastries'), 
    ('Cookies'), 
    ('Pies');

-- Insert into the 'boulangerie_user' table
INSERT INTO boulangerie_user (firstname, lastname, email, user_password) VALUES  
    ('Paul', 'Dupont', 'paul.dupont@example.com', 'password123'),
    ('Marie', 'Lemoine', 'marie.lemoine@example.com', 'password456'),
    ('Andry', 'Rakotomalala', 'andry.rakotomalala@example.com', 'password789');



INSERT INTO boulangerie_user (firstname, lastname, email, user_password) VALUES  
    ('Ninah', 'Malala', 'ninahx_x@cook.com', 'password123');
-- Insert into the 'ingredient' table
INSERT INTO ingredient (ingredient_name, unit, price) VALUES  
    ('Flour', 'grams', 1000),
    ('Sugar', 'grams', 500),
    ('Butter', 'grams', 1500),
    ('Eggs', 'pieces', 300),
    ('Yeast', 'grams', 50),
    ('Vanilla Extract', 'milliliters', 300),
    ('Salt', 'grams', 50);

-- Insert into the 'recipe' table
INSERT INTO recipe (title, recipe_description, id_category, cook_time, created_by) VALUES  
    ('Baguette', 'A traditional French baguette with a crispy crust and soft interior.',  1, '02:00:00', 'Paul Dupont'),
    ('Chocolate Croissant', 'A buttery and flaky croissant filled with rich chocolate.',  3, '00:45:00', 'Marie Lemoine'),
    ('Lemon Cake', 'A light and fluffy cake with a refreshing lemon flavor.',  2, '01:30:00', 'Andry Rakotomalala');

-- Insert into the 'recipe_ingredient' table
INSERT INTO recipe_ingredient (id_recipe, id_ingredient, quantity) VALUES  
    (1,  1,  500),
    (1,  2,  10),
    (1,  5,  5),
    (2,  1,  200),
    (2,  2,  100),
    (2,  3,  50),
    (3,  1,  300),
    (3,  2,  150),
    (3,  4,  3),
    (3,  7,  2);

-- Insert into the 'step' table
INSERT INTO step (id_recipe, step_number, instruction) VALUES  
    (1,  1, 'Mix the flour, yeast, and salt in a large bowl.'),
    (1,  2, 'Gradually add water and knead the dough until smooth.'),
    (1,  3, 'Let the dough rise for 1 hour, then shape into baguettes.'),
    (1,  4, 'Bake in a preheated oven at 220°C for 25 minutes.'),
    (2,  1, 'Roll out the croissant dough and cut into triangles.'),
    (2,  2, 'Place a piece of chocolate on each triangle and roll it up.'),
    (2,  3, 'Bake in the oven at 180°C for 15 minutes.'),
    (3,  1, 'Preheat the oven to 180°C.'),
    (3,  2, 'Mix the flour, sugar, eggs, and lemon juice into a batter.'),
    (3,  3, 'Pour the batter into a greased pan and bake for 45 minutes.');

-- Insert into the 'review' table
INSERT INTO review (id_user, id_recipe, rating, comment, review_date) VALUES
    (1, 1, 5, 'A perfect baguette, just like in France!', '2024-01-15'),
    (2, 2, 4, 'The croissant was delicious, but could use more chocolate.', '2024-01-20'),
    (3, 3, 5, 'The lemon cake was amazing! Perfect balance of sweetness and tartness.', '2024-01-25');
