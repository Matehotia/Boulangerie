package dao;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IngredientStock {
    private int idStock;
    private int idIngredient;
    private BigDecimal quantity;
    private LocalDate lastUpdate;
    private BigDecimal alertThreshold;
    private String modifiedBy;
    private String ingredientName;

    // Constructeurs
    public IngredientStock() {}

    public IngredientStock(int idIngredient, BigDecimal quantity) {
        this.idIngredient = idIngredient;
        this.quantity = quantity;
        this.lastUpdate = LocalDate.now();
    }

    public IngredientStock(int idIngredient, BigDecimal quantity, BigDecimal alertThreshold, String modifiedBy) {
        this.idIngredient = idIngredient;
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

    public int getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(int idIngredient) {
        this.idIngredient = idIngredient;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public BigDecimal getAlertThreshold() {
        return alertThreshold;
    }

    public void setAlertThreshold(BigDecimal alertThreshold) {
        this.alertThreshold = alertThreshold;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
    public static class StockDAO {
        // Obtenir le stock actuel d'un ingrédient
        public BigDecimal getCurrentStock(int idIngredient) throws SQLException {
            try (Connection conn = DBConnection.getPostgesConnection()) {
                String sql = "SELECT quantity FROM ingredient_stock WHERE id_ingredient = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, idIngredient);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return rs.getBigDecimal("quantity");
                    }
                }
            }
            return BigDecimal.ZERO;
        }

        // Mettre à jour le stock
        public void updateStock(int idIngredient, BigDecimal quantity, String modifiedBy) throws SQLException {
            try (Connection conn = DBConnection.getPostgesConnection()) {
                conn.setAutoCommit(false);
                try {
                    String sql = "UPDATE ingredient_stock SET quantity = ?, last_update = CURRENT_DATE, modified_by = ? " +
                               "WHERE id_ingredient = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setBigDecimal(1, quantity);
                        stmt.setString(2, modifiedBy);
                        stmt.setInt(3, idIngredient);
                        stmt.executeUpdate();
                    }
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                }
            }
        }

        // Enregistrer un mouvement de stock
        public void recordMovement(int idIngredient, String type, BigDecimal quantity, 
                                 String reason, String modifiedBy) throws SQLException {
            try (Connection conn = DBConnection.getPostgesConnection()) {
                conn.setAutoCommit(false);
                try {
                    // Enregistrer le mouvement
                    String sql = "INSERT INTO stock_movement (id_ingredient, movement_type, quantity, reason, modified_by) " +
                               "VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, idIngredient);
                        stmt.setString(2, type);
                        stmt.setBigDecimal(3, quantity);
                        stmt.setString(4, reason);
                        stmt.setString(5, modifiedBy);
                        stmt.executeUpdate();
                    }

                    // Mettre à jour le stock
                    BigDecimal currentStock = getCurrentStock(idIngredient);
                    BigDecimal newStock = type.equals("IN") ? 
                        currentStock.add(quantity) : 
                        currentStock.subtract(quantity);
                    
                    updateStock(idIngredient, newStock, modifiedBy);

                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                }
            }
        }

        // Obtenir les ingrédients sous le seuil d'alerte
        public List<IngredientStock> getLowStockIngredients() throws SQLException {
            List<IngredientStock> lowStock = new ArrayList<>();
            try (Connection conn = DBConnection.getPostgesConnection()) {
                String sql = "SELECT s.*, i.ingredient_name FROM ingredient_stock s " +
                           "JOIN ingredient i ON s.id_ingredient = i.id_ingredient " +
                           "WHERE s.quantity <= s.alert_threshold";
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        IngredientStock stock = new IngredientStock();
                        // Remplir l'objet stock...
                        lowStock.add(stock);
                    }
                }
            }
            return lowStock;
        }

        // Nouvelle méthode pour déduire les ingrédients utilisés dans une recette
        public void deductRecipeIngredients(int recipeId, int quantity, String modifiedBy) throws SQLException {
            try (Connection conn = DBConnection.getPostgesConnection()) {
                conn.setAutoCommit(false);
                try {
                    // Récupérer tous les ingrédients de la recette
                    String sql = "SELECT ri.id_ingredient, ri.quantity * ? as needed_quantity " +
                               "FROM recipe_ingredient ri " +
                               "WHERE ri.id_recipe = ?";
                    
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, quantity); // Multiplier par la quantité vendue
                        stmt.setInt(2, recipeId);
                        ResultSet rs = stmt.executeQuery();
                        
                        while (rs.next()) {
                            int ingredientId = rs.getInt("id_ingredient");
                            BigDecimal neededQuantity = rs.getBigDecimal("needed_quantity");
                            
                            // Déduire du stock
                            recordMovement(
                                ingredientId,
                                "OUT",
                                neededQuantity,
                                "Vente de recette #" + recipeId,
                                modifiedBy
                            );
                        }
                    }
                    
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                }
            }
        }

        // Vérifier si on a assez de stock pour une recette
        public boolean hasEnoughStock(int recipeId, int quantity) throws SQLException {
            try (Connection conn = DBConnection.getPostgesConnection()) {
                String sql = "SELECT ri.id_ingredient, " +
                           "(ri.quantity * ?) as needed_quantity, " +
                           "COALESCE(s.quantity, 0) as stock_quantity " +
                           "FROM recipe_ingredient ri " +
                           "LEFT JOIN ingredient_stock s ON ri.id_ingredient = s.id_ingredient " +
                           "WHERE ri.id_recipe = ?";
                
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, quantity);
                    stmt.setInt(2, recipeId);
                    ResultSet rs = stmt.executeQuery();
                    
                    while (rs.next()) {
                        BigDecimal needed = rs.getBigDecimal("needed_quantity");
                        BigDecimal inStock = rs.getBigDecimal("stock_quantity");
                        
                        if (inStock.compareTo(needed) < 0) {
                            return false; // Pas assez de stock
                        }
                    }
                }
            }
            return true; // Assez de stock pour tous les ingrédients
        }
    }
} 