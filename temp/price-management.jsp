<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="dao.Recipe, dao.PriceHistory, java.util.ArrayList" %>

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
                        <span class="text-muted fw-light">Gestion des prix /</span> 
                        <%= ((Recipe)request.getAttribute("recipe")).getTitle() %>
                    </h4>

                    <div class="row">
                        <div class="col-12">
                            <div class="card mb-4">
                                <div class="card-header">
                                    <h5 class="mb-0">Nouveau prix</h5>
                                </div>
                                <div class="card-body">
                                    <form method="post" action="price-management">
                                        <input type="hidden" name="recipeId" value="<%= ((Recipe)request.getAttribute("recipe")).getId() %>">
                                        <div class="mb-3">
                                            <label class="form-label">Prix</label>
                                            <div class="input-group">
                                                <input type="number" step="0.01" class="form-control" name="price" required>
                                                <span class="input-group-text">€</span>
                                            </div>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label">Commentaire</label>
                                            <textarea class="form-control" name="comment" rows="3"></textarea>
                                        </div>
                                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                                    </form>
                                </div>
                            </div>

                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0">Historique des prix</h5>
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th>Prix</th>
                                                <th>Date début</th>
                                                <th>Date fin</th>
                                                <th>Modifié par</th>
                                                <th>Commentaire</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% ArrayList<PriceHistory> priceHistory = (ArrayList<PriceHistory>)request.getAttribute("priceHistory");
                                               for(PriceHistory price : priceHistory) { %>
                                                <tr>
                                                    <td><%= price.getPrice() %> €</td>
                                                    <td><%= price.getStartsDate() %></td>
                                                    <td><%= price.getEndDate() != null ? price.getEndDate() : "-" %></td>
                                                    <td><%= price.getModifiedBy() %></td>
                                                    <td><%= price.getComment() %></td>
                                                </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>
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