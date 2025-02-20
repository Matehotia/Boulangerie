<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="dao.Recipe, java.util.ArrayList, java.math.BigDecimal" %>

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
                    <h4 class="fw-bold py-3 mb-4">Gestion des prix</h4>

                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0">Liste des recettes</h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Recette</th>
                                            <th>Prix actuel</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% ArrayList<Recipe> recipes = (ArrayList<Recipe>)request.getAttribute("recipes");
                                           for(Recipe recipe : recipes) { 
                                               // Forcer la récupération du prix actuel
                                               BigDecimal currentPrice = recipe.getCurrentPrice();
                                        %>
                                            <tr>
                                                <td><%= recipe.getTitle() %></td>
                                                <td><%= String.format("%.2f", currentPrice) %> €</td>
                                                <td>
                                                    <a href="price-management?recipeId=<%= recipe.getId() %>" class="btn btn-primary btn-sm">
                                                        <i class="bx bx-history"></i> Historique
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
                <!-- / Content -->
            </div>
            <!-- / Content wrapper -->
        </div>
        <!-- / Layout container -->
    </div>
</div>
<!-- / Layout wrapper -->

<%@include file="footer.jsp"%> 