package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import dao.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.Recipe;
import dao.Review;
import util.SessionUtils;

public class FormReviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtils.isUserConnected(req)) {
            resp.sendRedirect("form-login");
            return;
        }

        String action = req.getParameter("action");
        User connectedUser = SessionUtils.getConnectedUser(req);
        Review review = new Review();
        ArrayList<Recipe> recipes;

        try {
            recipes = Recipe.all();
        } catch (Exception e) {
            throw new ServletException(e);
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            review.setId(id);
            try {
                review.find();
            } catch (Exception e) {
                throw new ServletException(e);
            }
        } else {
            action = "create";
            review.setIdUser(connectedUser.getId());
        }

        if (review.getIdUser() != connectedUser.getId()) {
            resp.sendRedirect("review");
            return;
        }

        req.setAttribute("action", action);
        req.setAttribute("review", review);
        req.setAttribute("recipes", recipes);
        req.setAttribute("activeMenuItem", "review");
        req.setAttribute("pageTitle", "Retour");

        RequestDispatcher dispatcher = req.getRequestDispatcher("form-review.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String month = req.getParameter("month");
        String year = req.getParameter("year");

        // Log or process the received month and year
        System.out.println("Selected Month: " + month);
        System.out.println("Selected Year: " + year);

        try {
            int p= Integer.parseInt(month);
            if (p == 0) {
 
                ArrayList<Review> reviews  = Review.getReviewByear(year);    
                ArrayList<User> users = User.all();
                ArrayList<Recipe> recipes = Recipe.all();      
                req.setAttribute("reviews", reviews);
                req.setAttribute("users", users);
                req.setAttribute("recipes", recipes);
                req.setAttribute("activeMenuItem", "review");
                req.setAttribute("pageTitle", "Retour");
            }
            else{
                ArrayList<Review> reviews  = Review.getReviewByMonth(month, year);    
                ArrayList<User> users = User.all();
                ArrayList<Recipe> recipes = Recipe.all();      
                req.setAttribute("reviews", reviews);
                req.setAttribute("users", users);
                req.setAttribute("recipes", recipes);
                req.setAttribute("activeMenuItem", "review");
                req.setAttribute("pageTitle", "Retour");
            }

            RequestDispatcher dispatcher = req.getRequestDispatcher("review.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}