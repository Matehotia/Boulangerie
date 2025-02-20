package servlet;

import dao.*;  // Importer tous les DAO nécessaires
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class VenteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            
            if ("list".equals(action)) {
                String clientId = req.getParameter("clientId");
                String startDate = req.getParameter("startDate");
                String endDate = req.getParameter("endDate");
                
                // Charger la liste des ventes filtrée
                ArrayList<Vente> ventes = Vente.getFilteredVentes(clientId, startDate, endDate);
                req.setAttribute("ventes", ventes);
                
                // Charger les détails des ventes
                ArrayList<VenteDetails> venteDetails = VenteDetails.all();
                req.setAttribute("venteDetails", venteDetails);
                
                // Charger la liste des clients pour le filtre
                ArrayList<User> clients = User.getAllClients();
                req.setAttribute("clients", clients);
                
                req.getRequestDispatcher("venteDetails.jsp").forward(req, resp);
            } else {
                // Page de création de vente
                ArrayList<String[]> recipes = Vente.getAllRecipes();
                req.setAttribute("recipes", recipes);
                
                ArrayList<User> clients = User.getAllClients();
                req.setAttribute("clients", clients);
                
                req.getRequestDispatcher("vente.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException("Erreur lors du chargement de la page vente", e);
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

            // Récupérer les données de la vente
            int clientId = Integer.parseInt(request.getParameter("client"));
            String[] recipeIds = request.getParameterValues("products[]");
            String[] quantities = request.getParameterValues("quantities[]");
            String[] prices = request.getParameterValues("prices[]");

            // Vérifier le stock pour toutes les recettes
            IngredientStock.StockDAO ingredientStockDAO = new IngredientStock.StockDAO();
            RecipeStock.RecipeStockDAO recipeStockDAO = new RecipeStock.RecipeStockDAO();

            for (int i = 0; i < recipeIds.length; i++) {
                int recipeId = Integer.parseInt(recipeIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                
                // Vérifier le stock des ingrédients
                if (!ingredientStockDAO.hasEnoughStock(recipeId, quantity)) {
                    Recipe recipe = new Recipe(recipeId);
                    recipe.find();
                    request.setAttribute("error", "Stock d'ingrédients insuffisant pour : " + recipe.getTitle());
                    request.getRequestDispatcher("vente.jsp").forward(request, response);
                    return;
                }

                // Vérifier le stock des produits finis
                int currentStock = recipeStockDAO.getCurrentStock(recipeId);
                if (currentStock < quantity) {
                    Recipe recipe = new Recipe(recipeId);
                    recipe.find();
                    request.setAttribute("error", "Stock de produits finis insuffisant pour : " + recipe.getTitle() + 
                                        " (Disponible : " + currentStock + ")");
                    request.getRequestDispatcher("vente.jsp").forward(request, response);
                    return;
                }
            }

            // Créer la vente
            Vente vente = new Vente();
            vente.setClientId(clientId);
            vente.setVenteDate(new Timestamp(System.currentTimeMillis()));
            
            // Calculer le montant total
            double totalAmount = 0;
            for (int i = 0; i < prices.length; i++) {
                totalAmount += Double.parseDouble(prices[i]) * Integer.parseInt(quantities[i]);
            }
            vente.setTotalAmount(totalAmount);
            
            // Sauvegarder la vente
            vente.create();

            // Pour chaque recette vendue
            for (int i = 0; i < recipeIds.length; i++) {
                int recipeId = Integer.parseInt(recipeIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                double price = Double.parseDouble(prices[i]);

                // Créer le détail de la vente
                VenteDetails detail = new VenteDetails();
                detail.setVenteId(vente.getId());
                detail.setRecipeId(recipeId);
                detail.setQuantity(quantity);
                detail.setUnitPrice(price);
                detail.create();

                // Déduire les ingrédients du stock
                ingredientStockDAO.deductRecipeIngredients(recipeId, quantity, user.getEmail());

                // Déduire du stock des produits finis
                recipeStockDAO.recordSale(recipeId, quantity, user.getEmail());
            }

            response.sendRedirect("vente?action=list");
        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'enregistrement de la vente", e);
        }
    }
    
}