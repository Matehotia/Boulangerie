package dao;

import java.sql.*;
import java.util.ArrayList;

public class VenteDetails {
    private int venteId;
    private int recipeId;
    private int quantity;
    private double unitPrice;
    private String recipeName;

    // Constructor
    public VenteDetails(int venteId, int recipeId, int quantity, double unitPrice) {
        this.venteId = venteId;
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public VenteDetails() {}

    // Getters and setters
    public int getVenteId() { return venteId; }
    public void setVenteId(int venteId) { this.venteId = venteId; }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

    // Method to fetch all vente details
    public static ArrayList<VenteDetails> all() throws Exception {
        ArrayList<VenteDetails> venteDetails = new ArrayList<>();
        String query = 
            "SELECT vd.*, r.title as recipe_name " +
            "FROM vente_details vd " +
            "JOIN recipe r ON vd.id_recipe = r.id_recipe " +
            "ORDER BY vd.id_vente";
        
        try (Connection connection = DBConnection.getPostgesConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                VenteDetails detail = new VenteDetails(
                    resultSet.getInt("id_vente"),
                    resultSet.getInt("id_recipe"),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("unit_price")
                );
                detail.setRecipeName(resultSet.getString("recipe_name"));
                venteDetails.add(detail);
            }
        }
        
        return venteDetails;
    }

    // Method to create a vente detail
    public void create() throws Exception {
        String query = "INSERT INTO vente_details(id_vente, id_recipe, quantity, unit_price) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DBConnection.getPostgesConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, venteId);
            statement.setInt(2, recipeId);
            statement.setInt(3, quantity);
            statement.setDouble(4, unitPrice);
            statement.executeUpdate();
        }
    }

    // Method to update a vente detail
    public void update() throws Exception {
        String query = "UPDATE vente_details SET quantity = ?, unit_price = ? WHERE id_vente = ? AND id_recipe = ?";
        
        try (Connection connection = DBConnection.getPostgesConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, quantity);
            statement.setDouble(2, unitPrice);
            statement.setInt(3, venteId);
            statement.setInt(4, recipeId);
            statement.executeUpdate();
        }
    }

    // Method to delete a vente detail
    public void delete() throws Exception {
        String query = "DELETE FROM vente_details WHERE id_vente = ? AND id_recipe = ?";
        
        try (Connection connection = DBConnection.getPostgesConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setInt(1, venteId);
            statement.setInt(2, recipeId);
            statement.executeUpdate();
        }
    }

    public static ArrayList<VenteDetails> getByVenteId(int venteId) throws Exception {
        ArrayList<VenteDetails> details = new ArrayList<>();
        String query = "SELECT vd.*, r.title as recipe_name " +
                      "FROM vente_details vd " +
                      "JOIN recipe r ON vd.id_recipe = r.id_recipe " +
                      "WHERE vd.id_vente = ?";
        
        try (Connection conn = DBConnection.getPostgesConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, venteId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                VenteDetails detail = new VenteDetails(
                    rs.getInt("id_vente"),
                    rs.getInt("id_recipe"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price")
                );
                detail.setRecipeName(rs.getString("recipe_name"));
                details.add(detail);
            }
        }
        return details;
    }

    public static ArrayList<VenteDetails> getByCommissionId(int commissionId) throws Exception {
        ArrayList<VenteDetails> details = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getPostgesConnection();
            String query = 
                "SELECT vd.*, r.title as recipe_name " +
                "FROM vente_details vd " +
                "JOIN recipe r ON vd.id_recipe = r.id_recipe " +
                "JOIN vente v ON vd.id_vente = v.id_vente " +
                "JOIN voir_comissions vc ON v.id_vente = vc.id_vente " +
                "WHERE vc.id_commission = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, commissionId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                VenteDetails detail = new VenteDetails(
                    resultSet.getInt("id_vente"),
                    resultSet.getInt("id_recipe"),
                    resultSet.getInt("quantity"),
                    resultSet.getDouble("unit_price")
                );
                detail.setRecipeName(resultSet.getString("recipe_name"));
                details.add(detail);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return details;
    }
}