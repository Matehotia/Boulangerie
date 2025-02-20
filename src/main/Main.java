package main;

import dao.Category;
import dao.DBConnection;
import java.sql.Connection;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Obtenir une connexion à la base de données
            connection = DBConnection.getPostgesConnection();
            if (connection != null) {
                ArrayList<Category> CATS= Category.all();
                System.out.print(CATS.get(2).toString());
                System.out.println("Connexion réussie à la base de données PostgreSQL !");
            } else {
                System.out.println("Échec de la connexion à la base de données.");
            }
        } catch (Exception e) {
            // Gérer les exceptions (classe non trouvée, erreur SQL, etc.)
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermer la connexion si elle a été ouverte
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connexion fermée.");
                } catch (Exception e) {
                    System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
                }
            }
        }
    }
}
