package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class Recipe {
    
    private int id;
    private String title = "";
    private String description = "";
    private int idCategory = 1;
    private LocalTime cookTime = LocalTime.of(0, 0, 0);
    private String createdBy = "";
    private LocalDate createdDate = LocalDate.now();
    private double price;
    private BigDecimal currentPrice;

    
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter humanTimeFormatter = new DateTimeFormatterBuilder()
            .appendPattern("H")
            .appendLiteral(" heure ")
            .optionalStart()
            .appendPattern("m")
            .appendLiteral(" minute")
            .optionalEnd()
            .toFormatter(Locale.FRENCH);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter humanDateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);

    public Recipe() {}

    public Recipe(int id) {
        this.id = id;
    }

    public Recipe(String title, String description, int idCategory, LocalTime cookTime, String createdBy,
            LocalDate createdDate) {
        this.title = title;
        this.description = description;
        this.idCategory = idCategory;
        this.cookTime = cookTime;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }
    public static double getRecipePrice(int recipeId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        double price = 0.0;
    
        try {
            // Assuming DBConnection.getPostgesConnection() is a method to establish a connection
            connection = DBConnection.getPostgesConnection();
            
            // Modify SQL query to fetch the actual_price from the get_actual_price view
            String sql = "SELECT actual_price "
                       + "FROM get_actual_price "
                       + "WHERE id_recipe = ?";
            
            // Prepare the statement with the SQL query
            statement = connection.prepareStatement(sql);
            
            // Set the recipeId parameter
            statement.setInt(1, recipeId);
            
            // Execute the query
            resultSet = statement.executeQuery();
    
            // Check if a result is returned and retrieve the actual_price
            if (resultSet.next()) {
                price = resultSet.getDouble("actual_price");
            }
        } finally {
            // Ensure resources are closed
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    
        return price;
    }
    

    public Recipe(int id, String title, String description, int idCategory, LocalTime cookTime, String createdBy,
            LocalDate createdDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.idCategory = idCategory;
        this.cookTime = cookTime;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public static ArrayList<Recipe> search(
        String searchTitle,
        String searchDescription,
        int searchIdCategory,
        LocalTime minCookTime,
        LocalTime maxCookTime,
        String searchCreator,
        LocalDate minCreationDate,
        LocalDate maxCreationDate
    )throws Exception {
        ArrayList<Recipe> recipes = new ArrayList<>();
    
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
    
        try {
            connection = DBConnection.getPostgesConnection();
            
            // Start building the SQL query
            StringBuilder sql = new StringBuilder("SELECT * FROM recipe");
            sql.append(" WHERE title ILIKE ?");
            sql.append(" AND recipe_description ILIKE ?");
            
            // Add conditions if they are not null
            if (searchIdCategory != 0) {
                sql.append(" AND id_category = ?");
            }
            if (minCookTime != null) {
                sql.append(" AND cook_time >= ?");
            }
            if (maxCookTime != null) {
                sql.append(" AND cook_time <= ?");
            }

            sql.append(" AND created_by ILIKE ?");
            
            if (minCreationDate != null) {
                sql.append(" AND created_date >= ?");
            }
            if (maxCreationDate != null) {
                sql.append(" AND created_date <= ?");
            }

            // Prepare the statement with the dynamically built SQL
            statement = connection.prepareStatement(sql.toString());
            
            // Set the search parameters
            int paramIndex = 1;
            statement.setString(paramIndex, "%" + searchTitle.toLowerCase() + "%");
            paramIndex++;
            statement.setString(paramIndex, "%" + searchDescription + "%");
            paramIndex++;
            if (searchIdCategory != 0) {
                statement.setInt(paramIndex, searchIdCategory);
                paramIndex++;
            }
            // Set the searchMinTime and searchMaxTime parameters if they are not null
            if (minCookTime != null) {
                statement.setTime(paramIndex, Time.valueOf(minCookTime));
                paramIndex++;
            }
            if (maxCookTime != null) {
                statement.setTime(paramIndex, Time.valueOf(maxCookTime));
                paramIndex++;
            }
            statement.setString(paramIndex, "%" + searchCreator + "%");
            paramIndex++;
            if (minCreationDate != null) {
                statement.setDate(paramIndex, Date.valueOf(minCreationDate));
                paramIndex++;
            }
            if (maxCreationDate != null) {
                statement.setDate(paramIndex, Date.valueOf(maxCreationDate));
                paramIndex++;
            }
            
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt("id_recipe");
                String title = resultSet.getString("title");
                String description = resultSet.getString("recipe_description");
                int idCategory = resultSet.getInt("id_category");
                LocalTime cookTime = resultSet.getTime("cook_time").toLocalTime();
                String createdBy = resultSet.getString("created_by");
                LocalDate createdDate = resultSet.getDate("created_date").toLocalDate();
    
                recipes.add(
                    new Recipe(id, title, description, idCategory, cookTime, createdBy, createdDate)
                );
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    
        return recipes;
    }

    public void find() throws SQLException {
        try (Connection conn = DBConnection.getPostgesConnection()) {
            String sql = "SELECT * FROM recipe WHERE id_recipe = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    this.title = rs.getString("title");
                    // ... charger les autres attributs
                }
            }
        }
    }

    public void create() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getPostgesConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(
                "INSERT INTO recipe(title, recipe_description, id_category, cook_time, created_by, created_date)"
                + " VALUES (?, ?, ?, ?, ?, ?)"
            );
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setInt(3, idCategory);
            statement.setTime(4, Time.valueOf(cookTime));
            statement.setString(5, createdBy);
            statement.setDate(6, Date.valueOf(createdDate));
            statement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public void update() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getPostgesConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(
                "UPDATE recipe"
                + " SET title = ?, recipe_description = ?, id_category = ?, cook_time = ?, created_by = ?, created_date = ?"
                + " WHERE id_recipe = ?"
            );
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setInt(3, idCategory);
            statement.setTime(4, Time.valueOf(cookTime));
            statement.setString(5, createdBy);
            statement.setDate(6, Date.valueOf(createdDate));
            statement.setInt(7, id);
            statement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public void delete() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // Delete all the recipe's ingredient before deleting
            // the recipe itself
            RecipeIngredient recipeIngredient = new RecipeIngredient(id);
            recipeIngredient.deleteFromIdRecipe();

            // Delete all the recipe's review
            Review review = new Review();
            review.setIdRecipe(id);
            review.deleteFromIdRecipe();

            Step step = new Step();
            step.setIdRecipe(id);
            step.deleteFromIdRecipe();

            connection = DBConnection.getPostgesConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(
                "DELETE FROM recipe"
                + " WHERE id_recipe = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }

    public String getDescriptionExcerpt() {
        return description.length() < 21 ? description : description.substring(0, 21) + "...";
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getIdCategory() {
        return idCategory;
    }
    
    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }
    
    public LocalTime getCookTime() {
        return cookTime;
    }

    public String getFormattedCookTime() {
        return cookTime.format(timeFormatter);
    }

    public String getHumanFormattedCookTime() {
        return cookTime.format(humanTimeFormatter);
    }
    
    public void setCookTime(LocalTime cookTime) {
        this.cookTime = cookTime;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    public String getFormattedCreatedDate() {
        return createdDate.format(dateFormatter);
    }

    public String getHumanFormattedCreatedDate() {
        return createdDate.format(humanDateFormatter);
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Recipe [id=" + id + ", title=" + title + ", description=" + description + ", idCategory=" + idCategory
                + ", cookTime=" + cookTime + ", createdBy=" + createdBy + ", createdDate=" + createdDate + "]";
    }
    public static ArrayList<Recipe> search(
    String searchTitle,
    String searchDescription,
    int searchIdCategory,
    LocalTime minCookTime,
    LocalTime maxCookTime,
    String searchCreator,
    LocalDate minCreationDate,
    LocalDate maxCreationDate,
    int searchIngredientId
) throws Exception {
    ArrayList<Recipe> recipes = new ArrayList<>();

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
        connection = DBConnection.getPostgesConnection();

        // Construire la requête SQL avec jointure explicite pour l'ingrédient
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT r.* FROM recipe r")
           .append(" LEFT JOIN recipe_ingredient ri ON r.id_recipe = ri.id_recipe")
           .append(" WHERE r.title ILIKE ?")
           .append(" AND r.recipe_description ILIKE ?");

        if (searchIdCategory != 0) {
            sql.append(" AND r.id_category = ?");
        }
        if (minCookTime != null) {
            sql.append(" AND r.cook_time >= ?");
        }
        if (maxCookTime != null) {
            sql.append(" AND r.cook_time <= ?");
        }

        sql.append(" AND r.created_by ILIKE ?");

        if (minCreationDate != null) {
            sql.append(" AND r.created_date >= ?");
        }
        if (maxCreationDate != null) {
            sql.append(" AND r.created_date <= ?");
        }

        // Si un ingrédient est spécifié, ajouter la condition pour l'ingrédient
        if (searchIngredientId != 0) {
            sql.append(" AND ri.id_ingredient = ?");
        }

        // Préparer la requête avec les paramètres
        statement = connection.prepareStatement(sql.toString());

        int paramIndex = 1;
        statement.setString(paramIndex, "%" + searchTitle.toLowerCase() + "%");
        paramIndex++;
        statement.setString(paramIndex, "%" + searchDescription.toLowerCase() + "%");
        paramIndex++;
        if (searchIdCategory != 0) {
            statement.setInt(paramIndex, searchIdCategory);
            paramIndex++;
        }
        if (minCookTime != null) {
            statement.setTime(paramIndex, Time.valueOf(minCookTime));
            paramIndex++;
        }
        if (maxCookTime != null) {
            statement.setTime(paramIndex, Time.valueOf(maxCookTime));
            paramIndex++;
        }
        statement.setString(paramIndex, "%" + searchCreator.toLowerCase() + "%");
        paramIndex++;
        if (minCreationDate != null) {
            statement.setDate(paramIndex, Date.valueOf(minCreationDate));
            paramIndex++;
        }
        if (maxCreationDate != null) {
            statement.setDate(paramIndex, Date.valueOf(maxCreationDate));
            paramIndex++;
        }

        // Si un ingrédient est spécifié, ajouter ce paramètre
        if (searchIngredientId != 0) {
            statement.setInt(paramIndex, searchIngredientId);
        }

        // Exécuter la requête
        resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id_recipe");
            String title = resultSet.getString("title");
            String description = resultSet.getString("recipe_description");
            int idCategory = resultSet.getInt("id_category");
            LocalTime cookTime = resultSet.getTime("cook_time").toLocalTime();
            String createdBy = resultSet.getString("created_by");
            LocalDate createdDate = resultSet.getDate("created_date").toLocalDate();

            recipes.add(new Recipe(id, title, description, idCategory, cookTime, createdBy, createdDate));
        }
    } finally {
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    return recipes;
}


    public static ArrayList<Recipe> searchByIngredient(int searchIngredientId) throws Exception {
        ArrayList<Recipe> recipes = new ArrayList<>();
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DBConnection.getPostgesConnection();
    
            // Construire la requête pour rechercher les recettes par ingrédient
            String sql = "SELECT r.* FROM recipe r " +
                         "JOIN recipe_ingredient ri ON r.id_recipe = ri.id_recipe " +
                         "WHERE ri.id_ingredient = ?";
    
            // Préparer la requête avec les paramètres
            statement = connection.prepareStatement(sql);
            statement.setInt(1, searchIngredientId);
    
            // Exécuter la requête
            resultSet = statement.executeQuery();
    
            // Récupérer les résultats
            while (resultSet.next()) {
                int id = resultSet.getInt("id_recipe");
                String title = resultSet.getString("title");
                String description = resultSet.getString("recipe_description");
                int idCategory = resultSet.getInt("id_category");
                LocalTime cookTime = resultSet.getTime("cook_time").toLocalTime();
                String createdBy = resultSet.getString("created_by");
                LocalDate createdDate = resultSet.getDate("created_date").toLocalDate();
    
                recipes.add(new Recipe(id, title, description, idCategory, cookTime, createdBy, createdDate));
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        
        return recipes;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice()throws Exception {
        double d= Recipe.getRecipePrice(id);
        this.setPrice(d);
        return d;
    }

    public static DateTimeFormatter getTimeformatter() {
        return timeFormatter;
    }

    public static DateTimeFormatter getHumantimeformatter() {
        return humanTimeFormatter;
    }

    public static DateTimeFormatter getDateformatter() {
        return dateFormatter;
    }

    public static DateTimeFormatter getHumandateformatter() {
        return humanDateFormatter;
    }
    
    public static Recipe getById(int id) {
        try {
            Connection connection = DBConnection.getPostgesConnection();
            String query = "SELECT * FROM recipe WHERE id_recipe = ?";
            
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    Recipe recipe = new Recipe();
                    recipe.setId(rs.getInt("id_recipe"));
                    recipe.setTitle(rs.getString("title"));
                    // ... autres setters selon vos colonnes
                    return recipe;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigDecimal getCurrentPrice() {
        try {
            System.out.println("Getting price for recipe: " + this.getTitle() + " (ID: " + this.getId() + ")");
            PriceHistory.PriceHistoryDAO priceDAO = new PriceHistory.PriceHistoryDAO();
            BigDecimal price = priceDAO.getCurrentPrice(this.getId());
            System.out.println("Price retrieved: " + price);
            return price;
        } catch (SQLException e) {
            System.err.println("Error getting price for recipe " + this.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    public static ArrayList<Recipe> all() throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try (Connection connection = DBConnection.getPostgesConnection()) {
            String sql = "SELECT * FROM recipe ORDER BY title";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Recipe recipe = new Recipe();
                        recipe.setId(rs.getInt("id_recipe"));
                        recipe.setTitle(rs.getString("title"));
                        recipe.setDescription(rs.getString("recipe_description"));
                        recipe.setIdCategory(rs.getInt("id_category"));
                        recipe.setCookTime(rs.getTime("cook_time").toLocalTime());
                        recipe.setCreatedBy(rs.getString("created_by"));
                        recipe.setCreatedDate(rs.getDate("created_date").toLocalDate());
                        recipes.add(recipe);
                    }
                }
            }
        }
        return recipes;
    }
}