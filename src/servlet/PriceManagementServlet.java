package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.PriceHistory;
import dao.Recipe;
import util.SessionUtils;
import dao.User;

@WebServlet("/price-management")
public class PriceManagementServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // Vérifier si l'utilisateur est connecté
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/form-login.jsp");
                return;
            }

            String recipeIdStr = request.getParameter("recipeId");
            
            if (recipeIdStr == null) {
                // Si pas de recipeId, afficher la liste des recettes avec leurs prix
                List<Recipe> recipes = Recipe.all();
                
                // Debug: Afficher les prix actuels
                for (Recipe recipe : recipes) {
                    System.out.println("Recipe: " + recipe.getTitle() + 
                                     ", Current Price: " + recipe.getCurrentPrice());
                }
                
                request.setAttribute("recipes", recipes);
                request.getRequestDispatcher("price-list.jsp").forward(request, response);
                return;
            }
            
            int recipeId = Integer.parseInt(recipeIdStr);
            PriceHistory.PriceHistoryDAO priceDAO = new PriceHistory.PriceHistoryDAO();
            List<PriceHistory> history = priceDAO.getPriceHistory(recipeId);
            
            Recipe recipe = new Recipe(recipeId);
            recipe.find();
            
            request.setAttribute("recipe", recipe);
            request.setAttribute("priceHistory", history);
            request.getRequestDispatcher("price-management.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Erreur lors de la récupération de l'historique des prix: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/form-login.jsp");
                return;
            }

            int recipeId = Integer.parseInt(request.getParameter("recipeId"));
            BigDecimal newPrice = new BigDecimal(request.getParameter("price"));
            String comment = request.getParameter("comment");

            System.out.println("Inserting new price " + newPrice + " for recipe " + recipeId);
            
            PriceHistory.PriceHistoryDAO priceDAO = new PriceHistory.PriceHistoryDAO();
            priceDAO.insertNewPrice(recipeId, newPrice, user.getEmail(), comment);
            
            // Vérifier que le nouveau prix a bien été inséré
            BigDecimal currentPrice = priceDAO.getCurrentPrice(recipeId);
            System.out.println("After insert, current price is: " + currentPrice);

            // Rediriger vers la liste des prix
            response.sendRedirect(request.getContextPath() + "/price-management");
        } catch (Exception e) {
            System.err.println("Error in doPost: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erreur lors de la mise à jour du prix: " + e.getMessage(), e);
        }
    }
} 