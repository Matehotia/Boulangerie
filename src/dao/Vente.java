package dao;

import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import dao.DBConnection;
import dao.User;

public class Vente {
    private int id;
    private int clientId;
    private Timestamp venteDate;
    private double totalAmount;
    private String clientFirstname;
    private String clientLastname;

    public Vente(int clientId, Timestamp venteDate, double totalAmount) {
        this.clientId = clientId;
        this.venteDate = venteDate;
        this.totalAmount = totalAmount;
    }

    public Vente() {}

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    
    public Timestamp getVenteDate() { return venteDate; }
    public void setVenteDate(Timestamp venteDate) { this.venteDate = venteDate; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getClientFirstname() { return clientFirstname; }
    public void setClientFirstname(String clientFirstname) { this.clientFirstname = clientFirstname; }

    public String getClientLastname() { return clientLastname; }
    public void setClientLastname(String clientLastname) { this.clientLastname = clientLastname; }

    public String getClientFullName() {
        return clientFirstname + " " + clientLastname;
    }

    // Method to fetch all ventes
    public static ArrayList<Vente> all() throws Exception {
        ArrayList<Vente> ventes = new ArrayList<>();
        String query = "SELECT * FROM vente";
        
        try (Connection connection = DBConnection.getPostgesConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id_vente");
                int clientId = resultSet.getInt("id_user");
                Timestamp venteDate = resultSet.getTimestamp("vente_date");
                double totalAmount = resultSet.getDouble("total_amount");
                
                ventes.add(new Vente(clientId, venteDate, totalAmount));
            }
        }
        
        return ventes;
    }

    // Method to create a vente
    public int create() throws Exception {
        String query = "INSERT INTO vente (id_user, vente_date, total_amount) " +
                      "VALUES (?, ?, ?) " +
                      "RETURNING id_vente, vente_date";
        
        try (Connection conn = DBConnection.getPostgesConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, this.clientId);
            pstmt.setTimestamp(2, this.venteDate);
            pstmt.setDouble(3, this.totalAmount);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.id = rs.getInt("id_vente");
                    this.venteDate = rs.getTimestamp("vente_date");
                    return this.id;
                }
            }
        }
        return 0;
    }

    // Method to update a vente
    public void update() throws Exception {
        String query = "UPDATE vente SET id_user = ?, vente_date = ?, total_amount = ? WHERE id_vente = ?";
        
        try (Connection connection = DBConnection.getPostgesConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, clientId);
            statement.setTimestamp(2, venteDate);
            statement.setDouble(3, totalAmount);
            statement.setInt(4, id);
            statement.executeUpdate();
        }
    }

    // Method to delete a vente
    public void delete() throws Exception {
        String query = "DELETE FROM vente WHERE id_vente = ?";
        
        try (Connection connection = DBConnection.getPostgesConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void updateTotalAmount(double newTotalAmount) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getPostgesConnection();
            String query = "UPDATE vente SET total_amount = ? WHERE id_vente = ?";
            statement = connection.prepareStatement(query);
            statement.setDouble(1, newTotalAmount);
            statement.setInt(2, this.id);
            statement.executeUpdate();
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public static ArrayList<String[]> getSalesList() throws Exception {
        ArrayList<String[]> salesList = new ArrayList<>();
        
        String query = "SELECT v.id_vente, v.vente_date, " +
                      "COALESCE(u.firstname || ' ' || u.lastname, 'Client inconnu') AS client_name, " +
                      "v.total_amount " +
                      "FROM vente v " +
                      "LEFT JOIN boulangerie_user u ON v.id_user = u.id_user " +
                      "ORDER BY v.vente_date DESC";
        
        try (Connection connection = DBConnection.getPostgesConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                String[] sale = new String[4];
                sale[0] = String.valueOf(resultSet.getInt("id_vente"));
                sale[1] = resultSet.getString("vente_date");
                sale[2] = resultSet.getString("client_name");
                sale[3] = String.valueOf(resultSet.getDouble("total_amount"));
                salesList.add(sale);
            }
        }
        
        return salesList;
    }
    
    public static ArrayList<String[]> getFilteredSales(boolean isNature, String categoryName) throws Exception {
        ArrayList<String[]> filteredSales = new ArrayList<>();
        String query = "SELECT v.id_vente, v.vente_date, " +
                       "COALESCE(c.firstname || ' ' || c.lastname, 'Client inconnu') AS client_name, " +
                       "r.title AS recipe, cat.category_name, vn.is_nature, vd.quantity, vd.unit_price, " +
                       "(vd.quantity * vd.unit_price) AS sub_total, v.total_amount " +
                       "FROM vente v " +
                       "LEFT JOIN boulangerie_user c ON v.id_user = c.id_user " +
                       "JOIN vente_details vd ON v.id_vente = vd.id_vente " +
                       "JOIN recipe r ON vd.id_recipe = r.id_recipe " +
                       "JOIN category cat ON r.id_category = cat.id_category " +
                       "JOIN recipe_nature vn ON r.id_recipe = vn.id_recipe " +
                       "WHERE cat.category_name = ? AND vn.is_nature = ?";

        try (Connection connection = DBConnection.getPostgesConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, categoryName);
            statement.setBoolean(2, isNature);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String[] saleDetails = new String[10];
                    saleDetails[0] = String.valueOf(resultSet.getInt("id_vente"));
                    saleDetails[1] = resultSet.getString("vente_date");
                    saleDetails[2] = resultSet.getString("client_name");
                    saleDetails[3] = resultSet.getString("recipe");
                    saleDetails[4] = resultSet.getString("category_name");
                    saleDetails[5] = String.valueOf(resultSet.getBoolean("is_nature"));
                    saleDetails[6] = String.valueOf(resultSet.getInt("quantity"));
                    saleDetails[7] = String.valueOf(resultSet.getDouble("unit_price"));
                    saleDetails[8] = String.valueOf(resultSet.getDouble("sub_total"));
                    saleDetails[9] = String.valueOf(resultSet.getDouble("total_amount"));
                    filteredSales.add(saleDetails);
                }
            }
        }
        return filteredSales;
    }
    public static ArrayList<String[]> getFilteredparfum(int ingredientId, String categoryName) throws Exception {
        ArrayList<String[]> filteredSales = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
    
        try {
            connection = DBConnection.getPostgesConnection();
    
            // SQL query to get sales related to recipes containing the specific ingredient and category
            String query = "SELECT v.id_vente, v.vente_date, u.firstname || ' ' || u.lastname AS user_name, " +
                           "r.title AS recipe, c.category_name, vn.is_nature, vd.quantity, vd.unit_price, " +
                           "(vd.quantity * vd.unit_price) AS sub_total, v.total_amount " +
                           "FROM vente v " +
                           "LEFT JOIN boulangerie_user u ON v.id_user = u.id_user " +
                           "JOIN vente_details vd ON v.id_vente = vd.id_vente " +
                           "JOIN recipe r ON vd.id_recipe = r.id_recipe " +
                           "JOIN category c ON r.id_category = c.id_category " +
                           "JOIN recipe_nature vn ON r.id_recipe = vn.id_recipe " +
                           "JOIN recipe_ingredient ri ON r.id_recipe = ri.id_recipe " +
                           "JOIN ingredient i ON ri.id_ingredient = i.id_ingredient " +
                           "WHERE c.category_name = ? AND i.id_ingredient = ?";
    
            // Préparer la requête avec les paramètres de filtrage
            statement = connection.prepareStatement(query);
            statement.setString(1, categoryName);  // Catégorie
            statement.setInt(2, ingredientId);     // ID de l'ingrédient
    
            resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                String[] saleDetails = new String[10];  // Tableau pour stocker les détails de la vente
    
                // Remplir le tableau avec les données
                saleDetails[0] = String.valueOf(resultSet.getInt("id_vente"));
                saleDetails[1] = resultSet.getString("vente_date");
                saleDetails[2] = resultSet.getString("user_name");
                saleDetails[3] = resultSet.getString("recipe");
                saleDetails[4] = resultSet.getString("category_name");
                saleDetails[5] = String.valueOf(resultSet.getBoolean("is_nature"));
                saleDetails[6] = String.valueOf(resultSet.getInt("quantity"));
                saleDetails[7] = String.valueOf(resultSet.getDouble("unit_price"));
                saleDetails[8] = String.valueOf(resultSet.getDouble("sub_total"));
                saleDetails[9] = String.valueOf(resultSet.getDouble("total_amount"));
    
                // Ajouter les détails à la liste
                filteredSales.add(saleDetails);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    
        return filteredSales;
    }

    public static void filteparuser(ArrayList<String[]> ventes, int iduser) throws Exception {
        if (ventes == null) {
            return; // Si la liste est nulle, on ne fait rien
        }
    
        // Récupérer l'utilisateur en fonction de son ID
        User u = User.getById(iduser);
    
        // Construire le nom complet de l'utilisateur
        String name = u.getFirstname() + " " + u.getLastname();
    
        // Itérer sur la liste des ventes et supprimer celles qui ne correspondent pas au nom
        // et celles où le nom du client est null
        ventes.removeIf(sale -> sale[2] == null || !sale[2].equals(name)); // Vérifie si sale[2] est null ou ne correspond pas au nom
    }
    

    public static void filtrepardate(ArrayList<String[]> ventes, String date) throws Exception {
        if (ventes == null || date == null) {
            return; // Si la liste est nulle ou la date est nulle, on ne fait rien
        }
    
        // Itérer sur la liste des ventes et supprimer celles dont la vente_date ne correspond pas à la date
        ventes.removeIf(sale -> {
            // Extraire la partie date de vente_date (format : yyyy-MM-dd)
            String saleDate = sale[1].split(" ")[0]; // Récupère la date avant l'espace (la première partie de la chaîne)
    
            // Comparer la date de la vente avec la date donnée
            return !saleDate.equals(date); // Retourne true pour supprimer la vente
        });
    }
    
    public static ArrayList<String[]> getCommissionsByDateRange(String startDate, String endDate) throws Exception {
        ArrayList<String[]> commissions = new ArrayList<>();
        String query = 
            "SELECT " +
            "   u.id_user as id_vendeur, " +
            "   u.firstname || ' ' || u.lastname AS nom_vendeur, " +
            "   COUNT(DISTINCT v.id_vente) as total_ventes, " +
            "   COALESCE(SUM(v.total_amount), 0) as montant_total, " +
            "   COALESCE(c.valuee, 5) as pourcentage_commission, " +
            "   COALESCE(SUM(v.total_amount * COALESCE(c.valuee, 5) / 100), 0) as montant_commission " +
            "FROM boulangerie_user u " +
            "LEFT JOIN vente v ON u.id_user = v.id_user " +
            "   AND DATE(v.vente_date) >= ?::date " +
            "   AND DATE(v.vente_date) <= ?::date " +
            "LEFT JOIN commission c ON u.id_user = c.id_vendeur " +
            "   AND CURRENT_DATE BETWEEN c.date_debut AND c.date_fin " +
            "GROUP BY u.id_user, u.firstname, u.lastname, c.valuee " +
            "ORDER BY nom_vendeur";

        try (Connection conn = DBConnection.getPostgesConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            
            // Debug
            System.out.println("Requête SQL : " + query);
            System.out.println("Paramètres - startDate: " + startDate + ", endDate: " + endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String[] commission = new String[5];
                    commission[0] = rs.getString("nom_vendeur");
                    commission[1] = rs.getString("total_ventes");
                    commission[2] = rs.getDouble("montant_total") + "";
                    commission[3] = rs.getDouble("montant_commission") + "";
                    commission[4] = rs.getString("id_vendeur");
                    
                    // Debug
                    System.out.println("Commission trouvée - Vendeur: " + commission[0] + 
                                     ", Ventes: " + commission[1] + 
                                     ", Total: " + commission[2]);
                    
                    commissions.add(commission);
                }
            }
        }
        return commissions;
    }

    public static ArrayList<String[]> getVenteDetailsByClient(int clientId, String startDate, String endDate) throws Exception {
        ArrayList<String[]> details = new ArrayList<>();
        String query = 
            "SELECT " +
            "   v.vente_date, " +
            "   r.title as nom_recette, " +  // Nom de la recette
            "   vd.quantity as quantite, " +  // Quantité achetée
            "   vd.unit_price as prix_unitaire, " +  // Prix unitaire
            "   vd.quantity * vd.unit_price as sous_total, " +  // Sous-total
            "   COALESCE(c.valuee, 5) as pourcentage_commission, " +  // % commission
            "   (vd.quantity * vd.unit_price * COALESCE(c.valuee, 5) / 100) as commission_ligne " +  // Commission par ligne
            "FROM vente v " +
            "JOIN vente_details vd ON v.id_vente = vd.id_vente " +
            "JOIN recipe r ON vd.id_recipe = r.id_recipe " +  // Join avec les recettes
            "LEFT JOIN commission c ON v.id_user = c.id_vendeur " +
            "   AND v.vente_date BETWEEN c.date_debut AND c.date_fin " +
            "WHERE v.id_user = ? " +
            "   AND v.vente_date >= ?::date " +
            "   AND v.vente_date < (?::date + interval '1 day') " +
            "ORDER BY v.vente_date DESC, r.title";

        try (Connection conn = DBConnection.getPostgesConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, clientId);
            pstmt.setString(2, startDate);
            pstmt.setString(3, endDate);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] detail = new String[7];
                detail[0] = rs.getString("vente_date");
                detail[1] = rs.getString("nom_recette");
                detail[2] = rs.getString("quantite");
                detail[3] = String.format("%.2f", rs.getDouble("prix_unitaire")).replace(",", ".");
                detail[4] = String.format("%.2f", rs.getDouble("sous_total")).replace(",", ".");
                detail[5] = String.format("%.2f", rs.getDouble("pourcentage_commission")).replace(",", ".");
                detail[6] = String.format("%.2f", rs.getDouble("commission_ligne")).replace(",", ".");
                details.add(detail);
            }
        }
        return details;
    }

    public static ArrayList<String[]> getAllRecipes() throws Exception {
        ArrayList<String[]> recipes = new ArrayList<>();
        String query = 
            "SELECT r.id_recipe, r.title " +
            "FROM recipe r " +
            "ORDER BY r.title";

        try (Connection conn = DBConnection.getPostgesConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            System.out.println("Exécution de la requête pour récupérer les recettes");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String[] recipe = new String[3];
                recipe[0] = rs.getString("id_recipe");
                recipe[1] = rs.getString("title");
                recipe[2] = "0.00";  // Prix par défaut, sera modifié lors de la vente
                recipes.add(recipe);
                System.out.println("Recette trouvée : " + recipe[1]);
            }
            
            System.out.println("Nombre total de recettes : " + recipes.size());
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des recettes : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return recipes;
    }

    public static ArrayList<Vente> getAllWithUserInfo() throws Exception {
        ArrayList<Vente> ventes = new ArrayList<>();
        String query = 
            "SELECT v.*, u.firstname, u.lastname " +
            "FROM vente v " +
            "JOIN boulangerie_user u ON v.id_user = u.id_user " +
            "ORDER BY v.vente_date DESC";
        
        try (Connection conn = DBConnection.getPostgesConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vente vente = new Vente(
                    rs.getInt("id_user"),
                    rs.getTimestamp("vente_date"),
                    rs.getDouble("total_amount")
                );
                vente.setId(rs.getInt("id_vente"));
                vente.setClientFirstname(rs.getString("firstname"));
                vente.setClientLastname(rs.getString("lastname"));
                ventes.add(vente);
            }
        }
        return ventes;
    }

    // Méthode pour formater la date pour l'affichage
    public String getFormattedDate() {
        if (venteDate == null) return "";
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(venteDate);
    }

    public static ArrayList<Vente> getFilteredVentes(String clientId, String searchDate, String recipeId) throws Exception {
        ArrayList<Vente> ventes = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        ArrayList<Object> params = new ArrayList<>();

        if (recipeId != null && !recipeId.isEmpty()) {
            // Si on filtre par produit
            query.append(
                "SELECT DISTINCT v.*, u.firstname, u.lastname " +
                "FROM vente v " +
                "JOIN boulangerie_user u ON v.id_user = u.id_user " +
                "WHERE v.id_vente IN ( " +
                "   SELECT vd.id_vente " +
                "   FROM vente_details vd " +
                "   WHERE vd.id_recipe = ? " +
                ") "
            );
            params.add(Integer.parseInt(recipeId));
        } else {
            // Si on ne filtre pas par produit
            query.append(
                "SELECT v.*, u.firstname, u.lastname " +
                "FROM vente v " +
                "JOIN boulangerie_user u ON v.id_user = u.id_user " +
                "WHERE 1=1 "
            );
        }
        
        // Filtre par ID client
        if (clientId != null && !clientId.isEmpty()) {
            query.append("AND v.id_user = ? ");
            params.add(Integer.parseInt(clientId));
        }
        
        // Filtre par date exacte
        if (searchDate != null && !searchDate.isEmpty()) {
            query.append("AND DATE(v.vente_date) = ?::date ");
            params.add(searchDate);
        }
        
        query.append("ORDER BY v.vente_date DESC");
        
        try (Connection conn = DBConnection.getPostgesConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vente vente = new Vente(
                    rs.getInt("id_user"),
                    rs.getTimestamp("vente_date"),
                    rs.getDouble("total_amount")
                );
                vente.setId(rs.getInt("id_vente"));
                vente.setClientFirstname(rs.getString("firstname"));
                vente.setClientLastname(rs.getString("lastname"));
                ventes.add(vente);
            }
        }
        return ventes;
    }

    public static Vente getById(int id) throws Exception {
        String query = "SELECT v.*, u.firstname, u.lastname " +
                      "FROM vente v " +
                      "JOIN boulangerie_user u ON v.id_user = u.id_user " +
                      "WHERE v.id_vente = ?";
        
        try (Connection conn = DBConnection.getPostgesConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Vente vente = new Vente(
                    rs.getInt("id_user"),
                    rs.getTimestamp("vente_date"),
                    rs.getDouble("total_amount")
                );
                vente.setId(rs.getInt("id_vente"));
                vente.setClientFirstname(rs.getString("firstname"));
                vente.setClientLastname(rs.getString("lastname"));
                return vente;
            }
        }
        return null;
    }
}