package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecipeStock {
    private int idStock;
    private int idRecipe;
    private int quantity;
    private LocalDate lastUpdate;
    private int alertThreshold;
    private String modifiedBy;
    private String recipeName; // Pour l'affichage

    // Constructeurs
    public RecipeStock() {}

    public RecipeStock(int idRecipe, int quantity) {
        this.idRecipe = idRecipe;
        this.quantity = quantity;
        this.lastUpdate = LocalDate.now();
    }

    public RecipeStock(int idRecipe, int quantity, int alertThreshold, String modifiedBy) {
        this.idRecipe = idRecipe;
        this.quantity = quantity;
        this.alertThreshold = alertThreshold;
        this.modifiedBy = modifiedBy;
        this.lastUpdate = LocalDate.now();
    }

    // Getters et Setters
    public int getIdStock() {
        return idStock;
    }

    public void setIdStock(int idStock) {
        this.idStock = idStock;
    }

    public int getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getAlertThreshold() {
        return alertThreshold;
    }

    public void setAlertThreshold(int alertThreshold) {
        this.alertThreshold = alertThreshold;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public static class RecipeStockDAO {
        // Obtenir le stock actuel d'une recette
        public int getCurrentStock(int idRecipe) throws SQLException {
            try (Connection conn = DBConnection.getPostgesConnection()) {
                String sql = "SELECT quantity FROM recipe_stock WHERE id_recipe = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idRecipe);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return rs.getInt("quantity");
                    }
                }
            }
            return 0;
        }

        // Enregistrer une production
        public void recordProduction(int idRecipe, int quantity, String modifiedBy) throws SQLException {
            recordMovement(idRecipe, "PRODUCTION", quantity, "Production nouvelle", modifiedBy);
        }

        // Enregistrer une vente
        public void recordSale(int idRecipe, int quantity, String modifiedBy) throws SQLException {
            recordMovement(idRecipe, "VENTE", -quantity, "Vente", modifiedBy);
        }

        // Enregistrer un mouvement
        public void recordMovement(int idRecipe, String type, int quantity, String reason, String modifiedBy) throws SQLException {
            try (Connection conn = DBConnection.getPostgesConnection()) {
                conn.setAutoCommit(false);
                try {
                    // 1. Insérer le mouvement
                    String insertSql = "INSERT INTO recipe_stock_movement (id_recipe, movement_type, quantity, reason, modified_by) " +
                                     "VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                        stmt.setInt(1, idRecipe);
                        stmt.setString(2, type);
                        stmt.setInt(3, quantity);
                        stmt.setString(4, reason);
                        stmt.setString(5, modifiedBy);
                        stmt.executeUpdate();
                    }

                    // 2. Mettre à jour le stock
                    String updateSql = "INSERT INTO recipe_stock (id_recipe, quantity, modified_by) " +
                                     "VALUES (?, ?, ?) " +
                                     "ON CONFLICT (id_recipe) DO UPDATE " +
                                     "SET quantity = recipe_stock.quantity + ?, " +
                                     "last_update = CURRENT_DATE, " +
                                     "modified_by = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                        stmt.setInt(1, idRecipe);
                        stmt.setInt(2, quantity);
                        stmt.setString(3, modifiedBy);
                        stmt.setInt(4, quantity);
                        stmt.setString(5, modifiedBy);
                        stmt.executeUpdate();
                    }

                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                }
            }
        }

        // Obtenir l'historique des mouvements d'une recette
        public List<RecipeStockMovement> getMovementHistory(int idRecipe) throws SQLException {
            List<RecipeStockMovement> movements = new ArrayList<>();
            try (Connection conn = DBConnection.getPostgesConnection()) {
                String sql = "SELECT m.*, r.title as recipe_name " +
                           "FROM recipe_stock_movement m " +
                           "JOIN recipe r ON m.id_recipe = r.id_recipe " +
                           "WHERE m.id_recipe = ? " +
                           "ORDER BY m.movement_date DESC";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idRecipe);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        RecipeStockMovement movement = new RecipeStockMovement();
                        movement.setIdMovement(rs.getInt("id_movement"));
                        movement.setIdRecipe(rs.getInt("id_recipe"));
                        movement.setMovementType(rs.getString("movement_type"));
                        movement.setQuantity(rs.getInt("quantity"));
                        movement.setMovementDate(rs.getTimestamp("movement_date").toLocalDateTime());
                        movement.setReason(rs.getString("reason"));
                        movement.setModifiedBy(rs.getString("modified_by"));
                        movement.setRecipeName(rs.getString("recipe_name"));
                        movements.add(movement);
                    }
                }
            }
            return movements;
        }

        // Ajouter cette nouvelle méthode
        public List<RecipeStock> getAllRecipeStocks() throws SQLException {
            List<RecipeStock> stocks = new ArrayList<>();
            try (Connection conn = DBConnection.getPostgesConnection()) {
                String sql = "SELECT s.*, r.title as recipe_name " +
                            "FROM recipe_stock s " +
                            "JOIN recipe r ON s.id_recipe = r.id_recipe " +
                            "ORDER BY r.title";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        RecipeStock stock = new RecipeStock();
                        stock.setIdStock(rs.getInt("id_stock"));
                        stock.setIdRecipe(rs.getInt("id_recipe"));
                        stock.setQuantity(rs.getInt("quantity"));
                        stock.setLastUpdate(rs.getDate("last_update").toLocalDate());
                        stock.setAlertThreshold(rs.getInt("alert_threshold"));
                        stock.setModifiedBy(rs.getString("modified_by"));
                        stock.setRecipeName(rs.getString("recipe_name"));
                        stocks.add(stock);
                    }
                }
            }
            return stocks;
        }
    }
} 