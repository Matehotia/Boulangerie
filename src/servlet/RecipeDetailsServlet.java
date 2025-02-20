package servlet;

import dao.Recipe;
import dao.RecipeIngredient;
import dao.Step;
import dao.PriceHistory;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int idRecipe = req.getParameter("idRecipe") == null ? 0 : Integer.parseInt(req.getParameter("idRecipe"));

            Recipe recipe = new Recipe(idRecipe);
            recipe.find();
            ArrayList<Step> steps = Step.search(idRecipe, 0, 0, "");
            ArrayList<RecipeIngredient> recipeIngredients = RecipeIngredient.search(idRecipe);

            PriceHistory.PriceHistoryDAO priceDAO = new PriceHistory.PriceHistoryDAO();
            List<PriceHistory> priceHistory = priceDAO.getPriceHistory(idRecipe);

            req.setAttribute("recipe", recipe);
            req.setAttribute("steps", steps);
            req.setAttribute("recipeIngredients", recipeIngredients);
            req.setAttribute("activeMenuItem", "recipe");
            req.setAttribute("pageTitle", "Recette");
            req.setAttribute("priceHistory", priceHistory);

            RequestDispatcher dispatcher = req.getRequestDispatcher("recipe-details.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}