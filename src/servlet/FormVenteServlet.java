package servlet;


import dao.Category;
import dao.Recipe;
import dao.Vente;
import dao.VenteDetails;
import dao.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class FormVenteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Récupérer la liste des recettes
            ArrayList<Recipe> recipes = Recipe.all();
            req.setAttribute("recipes", recipes);

            // Récupérer la liste des clients
            ArrayList<User> clients = User.getAllClients(); // Il faudra créer cette méthode
            req.setAttribute("clients", clients);

            String p = "Formulaire vente";
            req.setAttribute("pageTitle", p);
            // Afficher le formulaire de saisie de vente
            RequestDispatcher dispatcher = req.getRequestDispatcher("form-vente.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Récupérer l'ID du client sélectionné
            int clientId = Integer.parseInt(req.getParameter("clientId"));
            double totalAmount = Double.parseDouble(req.getParameter("totalAmount"));

            // Créer la vente avec le client sélectionné
            // Passer null pour la date car elle sera générée par CURRENT_TIMESTAMP
            Vente vente = new Vente(clientId, null, totalAmount);
            int venteId = vente.create();

            // Récupérer les détails de la vente (recettes, quantités, prix unitaires)
            String[] recipeIds = req.getParameterValues("recipeId[]");
            String[] quantities = req.getParameterValues("quantity[]");
            String[] unitPrices = req.getParameterValues("unitPrice[]");
            
            // Log pour débogage
            System.out.println("recipeIds: " + Arrays.toString(recipeIds));
            System.out.println("quantities: " + Arrays.toString(quantities));
            System.out.println("unitPrices: " + Arrays.toString(unitPrices));

            if (recipeIds != null && quantities != null && unitPrices != null) {
                double calculatedTotalAmount = 0.0;

                for (int i = 0; i < recipeIds.length; i++) {
                    // Validation des données récupérées depuis le formulaire
                    int recipeId = Integer.parseInt(recipeIds[i]);
                    int quantity = Integer.parseInt(quantities[i]);
                    double unitPrice = Double.parseDouble(unitPrices[i]);

                    // Créer un détail de vente
                    VenteDetails venteDetails = new VenteDetails(venteId, recipeId, quantity, unitPrice);
                    venteDetails.create();

                    // Calculer le montant total en fonction des détails
                    calculatedTotalAmount += quantity * unitPrice;
                }

                // Mettre à jour le montant total dans la vente
                vente.updateTotalAmount(calculatedTotalAmount);
            }

            // Redirection vers la liste des ventes
            resp.sendRedirect("vente?action=list");

        } catch (Exception e) {
            throw new ServletException("Une erreur est survenue lors de l'enregistrement de la vente.", e);
        }
    }
    
}