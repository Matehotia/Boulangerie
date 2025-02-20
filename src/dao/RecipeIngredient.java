package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RecipeIngredient {
    private int idRecipe;
    private int idIngredient;
    private double quantity;
    private String ingredientName;
    private String ingredientUnit;

    public RecipeIngredient() {
    }

    public RecipeIngredient(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public RecipeIngredient(int idRecipe, int idIngredient) {
        this.idRecipe = idRecipe;
        this.idIngredient = idIngredient;
    }

    public RecipeIngredient(int idRecipe, int idIngredient, double quantity) {
        this.idRecipe = idRecipe;
        this.idIngredient = idIngredient;
        this.quantity = quantity;
    }

    public void deleteFromIdRecipe() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getPostgesConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(
                "DELETE FROM recipe_ingredient WHERE id_recipe = ?"
            );
            statement.setInt(1, idRecipe);
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

    public boolean find() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getPostgesConnection();
            statement = connection.prepareStatement(
                "SELECT * FROM recipe_ingredient WHERE id_recipe = ? AND id_ingredient = ?"
            );
            statement.setInt(1, idRecipe);
            statement.setInt(2, idIngredient);
            resultSet = statement.executeQuery();
            return resultSet.next();
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public boolean findByIdIngredient() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getPostgesConnection();
            statement = connection.prepareStatement(
                "SELECT * FROM recipe_ingredient WHERE id_ingredient = ?"
            );
            statement.setInt(1, idIngredient);
            resultSet = statement.executeQuery();
            return resultSet.next();
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }

    public void create() throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getPostgesConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(
                "INSERT INTO recipe_ingredient(id_recipe, id_ingredient, quantity) VALUES (?, ?, ?)"
            );
            statement.setInt(1, idRecipe);
            statement.setInt(2, idIngredient);
            statement.setDouble(3, quantity);
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
                "UPDATE recipe_ingredient SET quantity = ? WHERE id_recipe = ? AND id_ingredient = ?"
            );
            statement.setDouble(1, quantity);
            statement.setInt(2, idRecipe);
            statement.setInt(3, idIngredient);
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
            connection = DBConnection.getPostgesConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(
                "DELETE FROM recipe_ingredient WHERE id_recipe = ? AND id_ingredient = ?"
            );
            statement.setInt(1, idRecipe);
            statement.setInt(2, idIngredient);
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

    public static ArrayList<RecipeIngredient> search(int idRecipe) throws Exception {
        ArrayList<RecipeIngredient> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getPostgesConnection();
            statement = connection.prepareStatement(
                "SELECT * FROM recipe_ingredient WHERE id_recipe = ?"
            );
            statement.setInt(1, idRecipe);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                RecipeIngredient ri = new RecipeIngredient(
                    resultSet.getInt("id_recipe"),
                    resultSet.getInt("id_ingredient"),
                    resultSet.getDouble("quantity")
                );
                list.add(ri);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        return list;
    }

    public String getIngredientName() {
        try {
            Ingredient ingredient = new Ingredient(idIngredient);
            ingredient.find();
            return ingredient.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getIngredientUnit() {
        try {
            Ingredient ingredient = new Ingredient(idIngredient);
            ingredient.find();
            return ingredient.getUnit();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // Getters et Setters
    public int getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public int getIdIngredient() {
        return idIngredient;
    }

    public void setIdIngredient(int idIngredient) {
        this.idIngredient = idIngredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
} 