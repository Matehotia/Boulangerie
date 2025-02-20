-- Désactivation des contraintes de clé étrangère
SET session_replication_role = replica;

-- Suppression des données dans les tables enfants (ordre inverse des dépendances)
DELETE FROM review;
DELETE FROM step;
DELETE FROM recipe_ingredient;
DELETE FROM recipe;
DELETE FROM ingredient;
DELETE FROM category;
DELETE FROM boulangerie_user;

-- Réinitialisation des séquences des clés primaires
-- Cela permet de réinitialiser les valeurs des séquences associées aux colonnes SERIAL
ALTER SEQUENCE boulangerie_user_id_user_seq RESTART WITH 1;
ALTER SEQUENCE category_id_category_seq RESTART WITH 1;
ALTER SEQUENCE recipe_id_recipe_seq RESTART WITH 1;
ALTER SEQUENCE ingredient_id_ingredient_seq RESTART WITH 1;
ALTER SEQUENCE step_id_step_seq RESTART WITH 1;
ALTER SEQUENCE review_id_review_seq RESTART WITH 1;

-- Réactivation des contraintes de clé étrangère
SET session_replication_role = DEFAULT;