<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="dao.Vente, dao.VenteDetails, dao.User, java.util.ArrayList, util.SessionUtils" %>
<%
    ArrayList<VenteDetails> venteDetails = (ArrayList<VenteDetails>) request.getAttribute("venteDetails");
    ArrayList<Vente> ventes = (ArrayList<Vente>) request.getAttribute("ventes");
    ArrayList<User> clients = (ArrayList<User>) request.getAttribute("clients");
    ArrayList<String[]> recipes = (ArrayList<String[]>) request.getAttribute("recipes");
%>

<%@ include file="header.jsp" %>
<%@ include file="vertical-menu.jsp" %>

<div class="content-wrapper">
    <div class="container-fluid">
        <!-- En-tête -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card bg-gradient-info text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h3 class="card-title text-white mb-2">Liste des Ventes</h3>
                                <p class="mb-0">Historique complet des ventes</p>
                            </div>
                            <i class='bx bx-receipt' style="font-size: 3rem; opacity: 0.5"></i>
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
                        <form method="get" action="vente" class="row g-3">
                            <input type="hidden" name="action" value="list">
                            
                            <!-- Filtre par client -->
                            <div class="col-md-4">
                                <label class="form-label">Client</label>
                                <select name="clientId" class="form-select">
                                    <option value="">Tous les clients</option>
                                    <% if(clients != null) for(User client : clients) { %>
                                        <option value="<%= client.getId() %>" 
                                            <%= request.getParameter("clientId") != null && 
                                                request.getParameter("clientId").equals(String.valueOf(client.getId())) 
                                                ? "selected" : "" %>>
                                            <%= client.getFullName() %>
                                        </option>
                                    <% } %>
                                </select>
                            </div>

                            <!-- Filtre par date -->
                            <div class="col-md-3">
                                <label class="form-label">Date début</label>
                                <input type="date" name="startDate" class="form-control" 
                                       value="<%= request.getParameter("startDate") != null ? 
                                               request.getParameter("startDate") : "" %>">
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">Date fin</label>
                                <input type="date" name="endDate" class="form-control" 
                                       value="<%= request.getParameter("endDate") != null ? 
                                               request.getParameter("endDate") : "" %>">
                            </div>

                            <!-- Boutons -->
                            <div class="col-md-2 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary me-2">
                                    <i class="bx bx-filter-alt me-1"></i> Filtrer
                                </button>
                                <a href="vente?action=list" class="btn btn-outline-secondary">
                                    <i class="bx bx-reset me-1"></i> Reset
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Liste des ventes -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Date</th>
                                        <th>Client</th>
                                        <th>Produits</th>
                                        <th>Total</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if(ventes != null) for(Vente vente : ventes) { %>
                                    <tr>
                                        <td><%= vente.getId() %></td>
                                        <td><%= vente.getFormattedDate() %></td>
                                        <td><%= vente.getClientFullName() %></td>
                                        <td>
                                            <% if(venteDetails != null) { %>
                                                <ul class="list-unstyled mb-0">
                                                <% for(VenteDetails detail : venteDetails) { 
                                                    if(detail.getVenteId() == vente.getId()) { %>
                                                        <li><%= detail.getQuantity() %>x <%= detail.getRecipeName() %></li>
                                                    <% } 
                                                    } %>
                                                </ul>
                                            <% } %>
                                        </td>
                                        <td><%= String.format("%,.2f Ar", vente.getTotalAmount()) %></td>
                                        <td>
                                            <a href="vente?action=details&id=<%= vente.getId() %>" class="btn btn-sm btn-info">
                                                <i class="bx bx-detail"></i>
                                            </a>
                                        </td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function showVenteDetails(venteId) {
    window.location.href = 'vente?action=details&id=' + venteId;
}
</script>

<%@ include file="footer.jsp" %>