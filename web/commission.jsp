<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ include file="header.jsp" %>
<%@ include file="vertical-menu.jsp" %>
<%@ page import="dao.CommissionViewDAO" %>
<%@ page import="java.util.List" %>

<div class="content-wrapper">
    <div class="container-fluid">
        <!-- En-tête -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card bg-gradient-info text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h3 class="card-title text-white mb-2">Commissions des Vendeurs</h3>
                                <p class="mb-0">Suivi des commissions par période</p>
                            </div>
                            <i class='bx bx-dollar-circle' style="font-size: 3rem; opacity: 0.5"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Filtres -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <form method="get" action="commission" class="row g-3">
                            <div class="col-md-4">
                                <label class="form-label">Date début</label>
                                <input type="date" name="startDate" class="form-control" 
                                       value="<%= request.getAttribute("startDate") %>">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Date fin</label>
                                <input type="date" name="endDate" class="form-control" 
                                       value="<%= request.getAttribute("endDate") %>">
                            </div>
                            <div class="col-md-4 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary me-2">
                                    <i class="bx bx-filter-alt me-1"></i> Filtrer
                                </button>
                                <a href="commission" class="btn btn-outline-secondary">
                                    <i class="bx bx-reset me-1"></i> Reset
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Liste des commissions -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Vendeur</th>
                                        <th>Nombre de Ventes</th>
                                        <th>Montant Total</th>
                                        <th>Commission (%)</th>
                                        <th>Montant Commission</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                    List<CommissionViewDAO> commissions = (List<CommissionViewDAO>) request.getAttribute("commissions");
                                    if(commissions != null) {
                                        for(CommissionViewDAO commission : commissions) {
                                    %>
                                    <tr>
                                        <td><%= commission.getDateVente() %></td>
                                        <td><%= commission.getNomVendeur() %></td>
                                        <td><%= commission.getNombreVentes() %></td>
                                        <td><%= commission.getMontantTotal() %></td>
                                        <td><%= commission.getPourcentageCommission() %>%</td>
                                        <td><%= commission.getMontantCommission() %></td>
                                        <td>
                                            <a href="commission-details?id=<%= commission.getId() %>" class="btn btn-info btn-sm">
                                                <i class="bi bi-eye"></i> Détails
                                            </a>
                                        </td>
                                    </tr>
                                    <% 
                                        }
                                    }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>
</body>
</html> 