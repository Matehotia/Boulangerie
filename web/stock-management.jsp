<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="dao.*, java.util.List" %>

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
                    <h4 class="fw-bold py-3 mb-4">Gestion des Stocks</h4>

                    <% if (request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger">
                            <%= request.getAttribute("error") %>
                        </div>
                    <% } %>

                    <!-- Formulaire pour les ingrédients -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5>Mouvement de Stock - Ingrédients</h5>
                        </div>
                        <div class="card-body">
                            <form method="post" action="stock-management">
                                <input type="hidden" name="stockType" value="ingredient">
                                <div class="row">
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">Ingrédient</label>
                                        <select name="idIngredient" class="form-select" required>
                                            <% for(Ingredient ingredient : Ingredient.all()) { %>
                                                <option value="<%= ingredient.getId() %>">
                                                    <%= ingredient.getName() %>
                                                </option>
                                            <% } %>
                                        </select>
                                    </div>
                                    <div class="col-md-2 mb-3">
                                        <label class="form-label">Type</label>
                                        <select name="movementType" class="form-select" required>
                                            <option value="IN">Entrée</option>
                                            <option value="OUT">Sortie</option>
                                        </select>
                                    </div>
                                    <div class="col-md-2 mb-3">
                                        <label class="form-label">Quantité</label>
                                        <input type="number" step="0.01" name="quantity" class="form-control" required>
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label class="form-label">Raison</label>
                                        <input type="text" name="reason" class="form-control" required>
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary">Enregistrer</button>
                            </form>
                        </div>
                    </div>

                    <!-- Formulaire pour les recettes -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5>Production de Recettes</h5>
                        </div>
                        <div class="card-body">
                            <form method="post" action="stock-management">
                                <input type="hidden" name="stockType" value="recipe">
                                <div class="row">
                                    <div class="col-md-4">
                                        <label class="form-label">Recette</label>
                                        <select name="idRecipe" class="form-select" required>
                                            <option value="">Sélectionner une recette</option>
                                            <% for(Recipe recipe : (List<Recipe>)request.getAttribute("recipes")) { %>
                                                <option value="<%= recipe.getId() %>">
                                                    <%= recipe.getTitle() %>
                                                </option>
                                            <% } %>
                                        </select>
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label">Type</label>
                                        <select name="movementType" class="form-select" required>
                                            <option value="PRODUCTION">Production</option>
                                            <option value="ADJUSTMENT">Ajustement</option>
                                        </select>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label">Quantité</label>
                                        <input type="number" name="quantity" class="form-control" required>
                                    </div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Raison</label>
                                        <input type="text" name="reason" class="form-control">
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label">&nbsp;</label>
                                        <button type="submit" class="btn btn-primary d-block">Enregistrer</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Tableau des stocks bas -->
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <h5 class="card-title mb-0">Alertes Stock Bas</h5>
                            <a href="stock-history" class="btn btn-secondary btn-sm">
                                <i class="bx bx-history"></i> Historique
                            </a>
                        </div>
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Ingrédient</th>
                                        <th>Stock Actuel</th>
                                        <th>Seuil d'Alerte</th>
                                        <th>Dernière Mise à Jour</th>
                                        <th>État</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for(IngredientStock stock : (ArrayList<IngredientStock>)request.getAttribute("lowStock")) { %>
                                        <tr>
                                            <td><%= stock.getIngredientName() %></td>
                                            <td><%= stock.getQuantity() %></td>
                                            <td><%= stock.getAlertThreshold() %></td>
                                            <td><%= stock.getLastUpdate() %></td>
                                            <td>
                                                <span class="badge bg-danger">Stock Bas</span>
                                            </td>
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