package servlet;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.CommissionViewDAO;
import dao.VenteDetails;

public class CommissionDetailsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int commissionId = Integer.parseInt(req.getParameter("id"));
            
            // Récupérer les détails de la commission
            ArrayList<VenteDetails> details = VenteDetails.getByCommissionId(commissionId);
            CommissionViewDAO commission = CommissionViewDAO.getById(commissionId);
            
            req.setAttribute("commission", commission);
            req.setAttribute("details", details);
            req.setAttribute("pageTitle", "Détails Commission");
            req.setAttribute("activeMenuItem", "commission");
            
            RequestDispatcher dispatcher = req.getRequestDispatcher("/commission-details.jsp");
            dispatcher.forward(req, resp);
            
        } catch (Exception e) {
            System.err.println("Erreur dans CommissionDetailsServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erreur lors du chargement des détails de la commission", e);
        }
    }
} 