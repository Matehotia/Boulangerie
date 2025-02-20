package dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class PriceHistory {
    private int idPrice;
    private int idRecipe;
    private BigDecimal price;
    private LocalDate startsDate;
    private LocalDate endDate;
    private String modifiedBy;
    private String comment;

    // Constructeurs
    public PriceHistory() {}

    public PriceHistory(int idRecipe, BigDecimal price, String modifiedBy, String comment) {
        this.idRecipe = idRecipe;
        this.price = price;
        this.modifiedBy = modifiedBy;
        this.comment = comment;
        this.startsDate = LocalDate.now();
    }

    // Getters et Setters
    public int getIdPrice() {
        return idPrice;
    }

    public void setIdPrice(int idPrice) {
        this.idPrice = idPrice;
    }

    public int getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getStartsDate() {
        return startsDate;
    }

    public void setStartsDate(LocalDate startsDate) {
        this.startsDate = startsDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static class PriceHistoryDAO {
        public void insertNewPrice(int idRecipe, BigDecimal price, String modifiedBy, String comment) throws SQLException {
            try (Connection connection = DBConnection.getPostgesConnection()) {
                connection.setAutoCommit(false);
                try {
                    LocalDate today = LocalDate.now();
                    
                    // 1. Mettre à jour tous les anciens prix pour cette recette
                    String updateSql = "UPDATE price_history " +
                                     "SET end_date = ? " +
                                     "WHERE id_recipe = ? AND end_date IS NULL";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                        updateStmt.setDate(1, Date.valueOf(today.minusDays(1)));
                        updateStmt.setInt(2, idRecipe);
                        updateStmt.executeUpdate();
                    }

                    // 2. Insérer le nouveau prix
                    String insertSql = "INSERT INTO price_history " +
                                     "(id_recipe, price, starts_date, end_date, modified_by, comment) " +
                                     "VALUES (?, ?, ?, NULL, ?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                        insertStmt.setInt(1, idRecipe);
                        insertStmt.setBigDecimal(2, price);
                        insertStmt.setDate(3, Date.valueOf(today));
                        insertStmt.setString(4, modifiedBy);
                        insertStmt.setString(5, comment);
                        insertStmt.executeUpdate();
                    }

                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                }
            }
        }

        public List<PriceHistory> getPriceHistory(int idRecipe) throws SQLException {
            List<PriceHistory> history = new ArrayList<>();
            try (Connection connection = DBConnection.getPostgesConnection()) {
                String sql = "SELECT id_price, id_recipe, price, starts_date, end_date, modified_by, comment " +
                            "FROM price_history " +
                            "WHERE id_recipe = ? " +
                            "ORDER BY starts_date DESC";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, idRecipe);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            PriceHistory ph = new PriceHistory();
                            ph.setIdPrice(rs.getInt("id_price"));
                            ph.setIdRecipe(rs.getInt("id_recipe"));
                            ph.setPrice(rs.getBigDecimal("price"));
                            ph.setStartsDate(rs.getDate("starts_date").toLocalDate());
                            if (rs.getDate("end_date") != null) {
                                ph.setEndDate(rs.getDate("end_date").toLocalDate());
                            }
                            ph.setModifiedBy(rs.getString("modified_by"));
                            ph.setComment(rs.getString("comment"));
                            history.add(ph);
                        }
                    }
                }
            }
            return history;
        }

        public BigDecimal getCurrentPrice(int idRecipe) throws SQLException {
            try (Connection connection = DBConnection.getPostgesConnection()) {
                // Ajouter un log pour voir la requête
                System.out.println("Executing getCurrentPrice for recipe " + idRecipe);
                
                String sql = "SELECT ph.* FROM price_history ph " +
                            "WHERE ph.id_recipe = ? " +
                            "ORDER BY ph.starts_date DESC, ph.id_price DESC " + // Ajout de id_price pour garantir l'ordre
                            "LIMIT 1";
                
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, idRecipe);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            BigDecimal price = rs.getBigDecimal("price");
                            System.out.println("Found price: " + price + " for recipe " + idRecipe);
                            return price;
                        }
                    }
                }
            }
            System.out.println("No price found for recipe " + idRecipe);
            return BigDecimal.ZERO;
        }
    }
} 