<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%@ page import="dao.CommissionGenreDAO" %>

<!DOCTYPE html>
<html>
<head>
    <title>${pageTitle}</title>
    <%@ include file="header.jsp" %>
</head>
<body>
    <div class="wrapper">
        <!-- Menu Vertical -->
        <%@ include file="vertical-menu.jsp" %>

        <!-- Contenu Principal -->
        <div class="main-content">
            <h1>Commissions par Genre</h1>
            
            <form method="get" action="commission-genre" class="mb-4">
                <div class="row">
                    <div class="col-md-4">
                        <label for="dateDebut">Date début:</label>
                        <input type="date" id="dateDebut" name="dateDebut" 
                               value="${dateDebut}" class="form-control" required>
                    </div>
                    <div class="col-md-4">
                        <label for="dateFin">Date fin:</label>
                        <input type="date" id="dateFin" name="dateFin" 
                               value="${dateFin}" class="form-control" required>
                    </div>
                    <div class="col-md-4 align-self-end">
                        <button type="submit" class="btn btn-primary">Filtrer</button>
                    </div>
                </div>
            </form>

            <!-- Tableau résumé -->
            <div class="table-responsive mb-4">
                <h3>Résumé des commissions par genre</h3>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Genre</th>
                            <th>Total des commissions</th>
                            <th>Nombre de vendeurs</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                        List<CommissionGenreDAO> commissions = (List<CommissionGenreDAO>) request.getAttribute("commissions");
                        double totalFemmes = 0;
                        double totalHommes = 0;
                        int nbVendeursFemmes = 0;
                        int nbVendeursHommes = 0;
                        
                        if(commissions != null) {
                            for(CommissionGenreDAO commission : commissions) {
                                if("Femmes".equals(commission.getGenre())) {
                                    totalFemmes += commission.getTotalCommission().doubleValue();
                                    nbVendeursFemmes = commission.getNombreVendeurs();
                                } else {
                                    totalHommes += commission.getTotalCommission().doubleValue();
                                    nbVendeursHommes = commission.getNombreVendeurs();
                                }
                            }
                        }
                        %>
                        <tr>
                            <td>Femmes</td>
                            <td><%= String.format("%.2f €", totalFemmes) %></td>
                            <td><%= nbVendeursFemmes %></td>
                            <td>
                                <button class="btn btn-info btn-sm" onclick="toggleDetails('F')">
                                    Voir détails
                                </button>
                            </td>
                        </tr>
                        <tr>
                            <td>Hommes</td>
                            <td><%= String.format("%.2f €", totalHommes) %></td>
                            <td><%= nbVendeursHommes %></td>
                            <td>
                                <button class="btn btn-info btn-sm" onclick="toggleDetails('M')">
                                    Voir détails
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
<!-- Détails Femmes -->
<div id="detailsF" style="display: none;" class="mb-4">
    <h3>Détails des commissions - Femmes</h3>
    <%
    Map<String, List<CommissionGenreDAO>> detailsFemmesParVendeur = (Map<String, List<CommissionGenreDAO>>) request.getAttribute("detailsFemmesParVendeur");
    if (detailsFemmesParVendeur != null) {
        for (Map.Entry<String, List<CommissionGenreDAO>> entry : detailsFemmesParVendeur.entrySet()) {
            String nomVendeur = entry.getKey();
            List<CommissionGenreDAO> details = entry.getValue();
    %>
    <div>
        <h4><%= nomVendeur %></h4>
        <button class="btn btn-info btn-sm" onclick="toggleDetails('F_<%= nomVendeur %>')">
            Voir détails
        </button>
        <table id="detailsF_<%= nomVendeur %>" style="display: none;" class="table table-striped">
            <thead>
                <tr>
                    <th>Prix de vente</th>
                    <th>Taux de commission</th>
                    <th>Commission</th>
                    <th>Date de vente</th>
                </tr>
            </thead>
            <tbody>
                <%
                for (CommissionGenreDAO detail : details) {
                %>
                <tr>
                    <td><%= detail.getPrixVente() %> €</td>
                    <td><%= detail.getTauxCommission() %>%</td>
                    <td><%= detail.getCommission() %> €</td>
                    <td><%= detail.getDateVente() %></td>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>
    <%
        }
    }
    %>
</div>

<!-- Détails Hommes -->
<div id="detailsM" style="display: none;" class="mb-4">
    <h3>Détails des commissions - Hommes</h3>
    <%
    Map<String, List<CommissionGenreDAO>> detailsHommesParVendeur = (Map<String, List<CommissionGenreDAO>>) request.getAttribute("detailsHommesParVendeur");
    if (detailsHommesParVendeur != null) {
        for (Map.Entry<String, List<CommissionGenreDAO>> entry : detailsHommesParVendeur.entrySet()) {
            String nomVendeur = entry.getKey();
            List<CommissionGenreDAO> details = entry.getValue();
    %>
    <div>
        <h4><%= nomVendeur %></h4>
        <button class="btn btn-info btn-sm" onclick="toggleDetails('M_<%= nomVendeur %>')">
            Voir détails
        </button>
        <table id="detailsM_<%= nomVendeur %>" style="display: none;" class="table table-striped">
            <thead>
                <tr>
                    <th>Prix de vente</th>
                    <th>Taux de commission</th>
                    <th>Commission</th>
                    <th>Date de vente</th>
                </tr>
            </thead>
            <tbody>
                <%
                for (CommissionGenreDAO detail : details) {
                %>
                <tr>
                    <td><%= detail.getPrixVente() %> €</td>
                    <td><%= detail.getTauxCommission() %>%</td>
                    <td><%= detail.getCommission() %> €</td>
                    <td><%= detail.getDateVente() %></td>
                </tr>
                <%
                }
                %>
            </tbody>
        </table>
    </div>
    <%
        }
    }
    %>
</div>

<script>
function toggleDetails(id) {
    const detailsDiv = document.getElementById('details' + id);
    if (detailsDiv.style.display === 'none') {
        detailsDiv.style.display = 'block';
    } else {
        detailsDiv.style.display = 'none';
    }
}
</script>
</body>
</html>