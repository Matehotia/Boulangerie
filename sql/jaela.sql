CREATE TABLE Boulangerie_User(
   id_User INTEGER,
   Nom VARCHAR(50)  NOT NULL,
   Role VARCHAR(50)  NOT NULL,
   Email VARCHAR(50)  NOT NULL,
   Mpd VARCHAR(50)  NOT NULL,
   Genre BOOLEAN NOT NULL,
   PRIMARY KEY(id_User)
);

CREATE TABLE Vendeur(
   id_Vendeur INTEGER,
   Nom VARCHAR(50)  NOT NULL,
   Genre BOOLEAN NOT NULL,
   PRIMARY KEY(id_Vendeur)
);

CREATE TABLE Ingredients(
   id_Ingredient INTEGER,
   Nom VARCHAR(50)  NOT NULL,
   Unite VARCHAR(50)  NOT NULL,
   price INTEGER NOT NULL,
   is_nature BOOLEAN NOT NULL,
   PRIMARY KEY(id_Ingredient)
);

CREATE TABLE Vente(
   id_Vente INTEGER,
   vente_date DATE,
   prix_total NUMERIC(10,2)  ,
   id_Vendeur INTEGER NOT NULL,
   id_User INTEGER NOT NULL,
   PRIMARY KEY(id_Vente),
   FOREIGN KEY(id_Vendeur) REFERENCES Vendeur(id_Vendeur),
   FOREIGN KEY(id_User) REFERENCES Boulangerie_User(id_User)
);

CREATE TABLE Vente_details(
   id_Vente INTEGER,
   id_Recette INTEGER,
   Quantite INTEGER NOT NULL,
   prixUnitaire NUMERIC(10,2)   NOT NULL,
   id_Vendeur INTEGER NOT NULL,
   id_User INTEGER NOT NULL,
   PRIMARY KEY(id_Vente, id_Recette),
   FOREIGN KEY(id_Vendeur) REFERENCES Vendeur(id_Vendeur),
   FOREIGN KEY(id_User) REFERENCES Boulangerie_User(id_User)
);

CREATE TABLE Commission(
   id_Commission INTEGER,
   value_pourcentage NUMERIC(5,2)   NOT NULL,
   date_debut DATE NOT NULL,
   date_fin DATE NOT NULL,
   id_Vendeur INTEGER NOT NULL,
   PRIMARY KEY(id_Commission),
   FOREIGN KEY(id_Vendeur) REFERENCES Vendeur(id_Vendeur)
);

CREATE TABLE Category(
   id_Category INTEGER,
   category_name VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_Category)
);

CREATE TABLE Recette(
   id_Recette INTEGER,
   Title VARCHAR(50)  NOT NULL,
   recette_description VARCHAR(50)  NOT NULL,
   cook_time TIME NOT NULL,
   created_by VARCHAR(50)  NOT NULL,
   created_date DATE NOT NULL,
   id_Category INTEGER NOT NULL,
   PRIMARY KEY(id_Recette),
   FOREIGN KEY(id_Category) REFERENCES Category(id_Category)
);

CREATE TABLE Etape(
   id_Etape INTEGER,
   Num_etape INTEGER NOT NULL,
   Instruction VARCHAR(50)  NOT NULL,
   id_Recette INTEGER NOT NULL,
   PRIMARY KEY(id_Etape),
   FOREIGN KEY(id_Recette) REFERENCES Recette(id_Recette)
);

CREATE TABLE Retours(
   id_Retour INTEGER,
   Note INTEGER NOT NULL,
   Commentaire VARCHAR(50)  NOT NULL,
   retour_date DATE NOT NULL,
   id_User INTEGER NOT NULL,
   id_Recette INTEGER NOT NULL,
   PRIMARY KEY(id_Retour),
   FOREIGN KEY(id_User) REFERENCES Boulangerie_User(id_User),
   FOREIGN KEY(id_Recette) REFERENCES Recette(id_Recette)
);

CREATE TABLE historique_prix(
   id_Prix INTEGER,
   prix NUMERIC(10,2)   NOT NULL,
   date_debut DATE NOT NULL,
   date_fin DATE NOT NULL,
   id_Recette INTEGER NOT NULL,
   PRIMARY KEY(id_Prix),
   FOREIGN KEY(id_Recette) REFERENCES Recette(id_Recette)
);

CREATE TABLE recette_ingredients(
   id_Ingredient INTEGER,
   id_Recette INTEGER,
   PRIMARY KEY(id_Ingredient, id_Recette),
   FOREIGN KEY(id_Ingredient) REFERENCES Ingredients(id_Ingredient),
   FOREIGN KEY(id_Recette) REFERENCES Recette(id_Recette)
);
