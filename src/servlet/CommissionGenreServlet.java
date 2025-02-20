package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import dao.CommissionGenreDAO;
import java.math.BigDecimal;

public class CommissionGenreServlet extends HttpServlet {

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
    try {
        String dateDebut = req.getParameter("dateDebut");
        String dateFin = req.getParameter("dateFin");

        // Valeurs par défaut si non spécifiées
        if (dateDebut == null) {
            dateDebut = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        if (dateFin == null) {
            dateFin = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        // Récupérer les commissions par genre
        List<CommissionGenreDAO> commissions = CommissionGenreDAO.getCommissionsByPeriode(dateDebut, dateFin);

        // Récupérer les détails des ventes groupés par vendeur pour chaque genre
        Map<String, List<CommissionGenreDAO>> detailsFemmesParVendeur = CommissionGenreDAO.getDetailsVentesByGenre("F", dateDebut, dateFin);
        Map<String, List<CommissionGenreDAO>> detailsHommesParVendeur = CommissionGenreDAO.getDetailsVentesByGenre("M", dateDebut, dateFin);
        // Préparer les données pour la vue
        req.setAttribute("commissions", commissions);
        req.setAttribute("detailsFemmesParVendeur", detailsFemmesParVendeur);
        req.setAttribute("detailsHommesParVendeur", detailsHommesParVendeur);
        req.setAttribute("dateDebut", dateDebut);
        req.setAttribute("dateFin", dateFin);
        req.setAttribute("pageTitle", "Commissions par Genre");
        req.setAttribute("activeMenuItem", "commission-genre");

        // Forward vers la JSP
        RequestDispatcher dispatcher = req.getRequestDispatcher("/commission-genre.jsp");
        dispatcher.forward(req, resp);

    } catch (Exception e) {
        System.err.println("Erreur dans CommissionGenreServlet: " + e.getMessage());
        e.printStackTrace();
        throw new ServletException("Erreur lors du chargement des commissions par genre", e);
    }
}
}