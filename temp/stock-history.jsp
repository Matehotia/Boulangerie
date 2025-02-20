<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="dao.StockMovement, java.util.ArrayList" %>

<%@include file="header.jsp"%>

<!-- Layout wrapper -->
<div class="layout-wrapper layout-content-navbar">
    <div class="layout-container">
        <%@include file="vertical-menu.jsp"%>

        <!-- Layout container -->
        <div class="layout-page">
            <!-- Content wrapper -->
            <div class="content-wrapper">
                <!-- Content -->
                <div class="container-xxl flex-grow-1 container-p-y">
                    <h4 class="fw-bold py-3 mb-4">
                        <span class="text-muted fw-light">Stocks /</span> Historique des Mouvements
                    </h4>

                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Historique des Mouvements</h5>
                        </div>
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Ingrédient</th>
                                        <th>Type</th>
                                        <th>Quantité</th>
                                        <th>Raison</th>
                                        <th>Modifié par</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for(StockMovement movement : (ArrayList<StockMovement>)request.getAttribute("movements")) { %>
                                        <tr>
                                            <td><%= movement.getMovementDate() %></td>
                                            <td><%= movement.getIngredientName() %></td>
                                            <td>
                                                <% if(movement.getMovementType().equals("IN")) { %>
                                                    <span class="badge bg-success">Entrée</span>
                                                <% } else { %>
                                                    <span class="badge bg-warning">Sortie</span>
                                                <% } %>
                                            </td>
                                            <td><%= movement.getQuantity() %></td>
                                            <td><%= movement.getReason() %></td>
                                            <td><%= movement.getModifiedBy() %></td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <!-- / Content -->
            </div>
            <!-- / Content wrapper -->
        </div>
        <!-- / Layout container -->
    </div>
</div>
<!-- / Layout wrapper -->

<%@include file="footer.jsp"%> 