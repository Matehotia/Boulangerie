package servlet;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class StockManagementServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect("form-login.jsp");
                return;
            }

            // Stock des ingrédients
            IngredientStock.StockDAO ingredientStockDAO = new IngredientStock.StockDAO();
            request.setAttribute("lowIngredientStock", ingredientStockDAO.getLowStockIngredients());

            // Stock des recettes
            RecipeStock.RecipeStockDAO recipeStockDAO = new RecipeStock.RecipeStockDAO();
            request.setAttribute("recipeStocks", recipeStockDAO.getAllRecipeStocks());
            
            // Liste des ingrédients et recettes pour les formulaires
            request.setAttribute("ingredients", Ingredient.all());
            request.setAttribute("recipes", Recipe.all());

            request.getRequestDispatcher("stock-management.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Erreur lors de la récupération des stocks", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect("form-login.jsp");
                return;
            }

            String stockType = request.getParameter("stockType"); // "ingredient" ou "recipe"
            
            if ("ingredient".equals(stockType)) {
                // Gestion du stock d'ingrédients
                int idIngredient = Integer.parseInt(request.getParameter("idIngredient"));
                String movementType = request.getParameter("movementType");
                BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
                String reason = request.getParameter("reason");

                IngredientStock.StockDAO stockDAO = new IngredientStock.StockDAO();
                stockDAO.recordMovement(idIngredient, movementType, quantity, reason, user.getEmail());
            } 
            else if ("recipe".equals(stockType)) {
                // Gestion du stock de recettes
                int idRecipe = Integer.parseInt(request.getParameter("idRecipe"));
                String movementType = request.getParameter("movementType");
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                String reason = request.getParameter("reason");

                RecipeStock.RecipeStockDAO recipeStockDAO = new RecipeStock.RecipeStockDAO();
                
                if ("PRODUCTION".equals(movementType)) {
                    // Vérifier si on a assez d'ingrédients pour la production
                    IngredientStock.StockDAO ingredientStockDAO = new IngredientStock.StockDAO();
                    if (!ingredientStockDAO.hasEnoughStock(idRecipe, quantity)) {
                        request.setAttribute("error", "Stock d'ingrédients insuffisant pour cette production");
                        doGet(request, response);
                        return;
                    }
                    
                    // Déduire les ingrédients utilisés
                    ingredientStockDAO.deductRecipeIngredients(idRecipe, quantity, user.getEmail());
                    
                    // Enregistrer la production
                    recipeStockDAO.recordProduction(idRecipe, quantity, user.getEmail());
                } 
                else if ("ADJUSTMENT".equals(movementType)) {
                    // Au lieu d'utiliser recordMovement directement, utilisons recordProduction ou recordSale
                    if (quantity >= 0) {
                        recipeStockDAO.recordProduction(idRecipe, quantity, user.getEmail());
                    } else {
                        recipeStockDAO.recordSale(idRecipe, Math.abs(quantity), user.getEmail());
                    }
                }
            }

            response.sendRedirect("stock-management");
        } catch (Exception e) {
            throw new ServletException("Erreur lors de la mise à jour du stock", e);
        }
    }
} 