--get info user connected



---chaque user obtien commisssion 5% sur une peridode donnee


CREATE TABLE commission (
    id_commission SERIAL PRIMARY KEY,    -- Identifiant unique de la commission
    valuee DECIMAL(5, 2) NOT NULL CHECK (valuee >= 0 AND valuee <= 100), -- Pourcentage de la commission
    date_debut TIMESTAMP NOT NULL,       -- Date de début de validité de la commission
    date_fin TIMESTAMP NOT NULL,         -- Date de fin de validité de la commission
    id_vendeur INT NOT NULL,             -- Identifiant du vendeur (lien avec le vendeur)
    FOREIGN KEY (id_vendeur) REFERENCES boulangerie_user(id_user) -- Clé étrangère vers le vendeur
);


CREATE OR REPLACE VIEW voir_comissions AS
SELECT
    c.id_commission,
    c.id_vendeur,
    v.id_vente,
    v.total_amount,
    c.valuee AS pourcentage_commission,
    v.vente_date,
    (v.total_amount * c.valuee / 100) AS montant_commission
FROM
    vente v
INNER JOIN
    commission c ON v.id_user = c.id_vendeur
WHERE
    v.vente_date BETWEEN c.date_debut AND c.date_fin;

-- Ajout de la colonne genre à la table boulangerie_user
ALTER TABLE boulangerie_user
ADD COLUMN genre CHAR(1) CHECK (genre IN ('F', 'M'));

-- Mise à jour des genres pour les utilisateurs existants (exemple)
UPDATE boulangerie_user
SET genre = 
    CASE 
        WHEN id_user IN (1, 3, 5) THEN 'F'
        ELSE 'M'
    END;

-- Création de la vue pour les commissions par genre
CREATE OR REPLACE VIEW commission_par_genre AS
SELECT 
    u.genre,
    SUM(v.total_amount * c.valuee / 100) as total_commission,
    COUNT(DISTINCT u.id_user) as nombre_vendeurs,
    TO_CHAR(v.vente_date, 'YYYY-MM') as periode
FROM 
    boulangerie_user u
    JOIN vente v ON u.id_user = v.id_user
    JOIN commission c ON u.id_user = c.id_vendeur
WHERE
    v.vente_date BETWEEN c.date_debut AND c.date_fin
GROUP BY 
    u.genre, TO_CHAR(v.vente_date, 'YYYY-MM');

-- Exemple d'utilisation de la vue
SELECT 
    CASE 
        WHEN genre = 'F' THEN 'Femmes'
        WHEN genre = 'M' THEN 'Hommes'
    END as Genre,
    total_commission as "Total des commissions",
    nombre_vendeurs as "Nombre de vendeurs",
    periode as "Période"
FROM 
    commission_par_genre
WHERE 
    periode BETWEEN '2024-01' AND '2024-12';