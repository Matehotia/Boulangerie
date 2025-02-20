package servlet;

import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.Vente;
import java.time.LocalDate;
import dao.User;
import java.util.List;
import java.math.BigDecimal;
import jakarta.servlet.annotation.WebServlet;
import dao.CommissionViewDAO;

public class CommissionServlet extends HttpServlet {

  @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
        // Récupérer les paramètres de filtrage
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        // Valeurs par défaut si les dates ne sont pas spécifiées
        if (startDateStr == null || startDateStr.isEmpty()) {
            startDateStr = LocalDate.now().withDayOfMonth(1).toString(); // Premier jour du mois
        }
        if (endDateStr == null || endDateStr.isEmpty()) {
            endDateStr = LocalDate.now().toString(); // Aujourd'hui
        }

        // Récupérer les commissions filtrées par date
        List<CommissionViewDAO> commissions = CommissionViewDAO.getAll(startDateStr, endDateStr);
        System.out.println("Nombre de commissions trouvées: " + commissions.size());

        // Calculer le total des commissions
        BigDecimal totalCommissions = BigDecimal.ZERO;
        for (CommissionViewDAO commission : commissions) {
            if (commission != null && commission.getMontantCommission() != null) {
                totalCommissions = totalCommissions.add(commission.getMontantCommission());
            }
        }

        // Préparer les données pour la vue
        req.setAttribute("commissions", commissions);
        req.setAttribute("totalCommissions", totalCommissions);
        req.setAttribute("startDate", startDateStr);
        req.setAttribute("endDate", endDateStr);
        req.setAttribute("pageTitle", "Commissions");
        req.setAttribute("activeMenuItem", "commission");

        // Forward vers la JSP
        RequestDispatcher dispatcher = req.getRequestDispatcher("/commission.jsp");
        dispatcher.forward(req, resp);

    } catch (Exception e) {
        System.err.println("Erreur dans CommissionServlet: " + e.getMessage());
        e.printStackTrace();
        throw new ServletException("Erreur lors du chargement des commissions", e);
    }
}
}