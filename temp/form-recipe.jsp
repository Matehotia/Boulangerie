<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="dao.Recipe, dao.Category, java.util.ArrayList" %>
<%@ page import="util.SessionUtils" %>

<% 
    Recipe recipe = (Recipe) request.getAttribute("recipe");
    if (recipe == null) recipe = new Recipe();
%>

<%@include file="header.jsp"%>

<div class="layout-wrapper layout-content-navbar">
    <div class="layout-container">
        <%@include file="vertical-menu.jsp"%>
        <div class="layout-page">
            <div class="content-wrapper">
                <div class="container-xxl flex-grow-1 container-p-y">
                    <div class="row">
                        <div class="col-xxl">
                            <div class="card mb-4">
                                <div class="card-header d-flex align-items-center justify-content-between">
                                    <h5 class="mb-0">
                                        <%= request.getAttribute("action").equals("update") ? "Modifier" : "Nouvelle" %> recette
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <form method="post" action="recipe">
                                        <input type="hidden" name="action" 
                                               value="<%= request.getAttribute("action") %>" />
                                        
                                        <% if (request.getAttribute("action").equals("update")) { %>
                                            <input type="hidden" name="idRecipe" 
                                                   value="<%= recipe.getId() %>" />
                                        <% } %>

                                        <!-- Champs de base -->
                                        <div class="mb-3">
                                            <label class="form-label" for="recipeTitle">Titre</label>
                                            <input value="<%= recipe.getTitle() %>" 
                                                   name="recipeTitle" 
                                                   type="text" 
                                                   class="form-control" 
                                                   id="recipeTitle" 
                                                   required />
                                        </div>
                                        <div class="mb-3">
                                            <label for="recipeDescription" class="form-label">Description</label>
                                            <textarea name="recipeDescription" class="form-control" id="recipeDescription"
                                                      rows="3" required><%= recipe.getDescription() %></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="recipeIdCategory" class="form-label">Catégorie</label>
                                            <select name="recipeIdCategory" id="recipeIdCategory" class="form-select" required>
                                                <% for (Category category : (ArrayList<Category>) request.getAttribute("categories")) { %>
                                                    <option value="<%= category.getId() %>"
                                                            <% if (category.getId() == recipe.getIdCategory()) { %>selected<% } %>
                                                    >
                                                        <%= category.getName() %>
                                                    </option>
                                                <% } %>
                                            </select>
                                        </div>
                                        <div class="mb-3">
                                            <div class="d-flex justify-content-between">
                                                <label for="recipeCookTime" class="form-label">Temps de préparation</label>
                                                <small>heure:minute</small>
                                            </div>
                                            <input value="<%= recipe.getFormattedCookTime() %>" name="recipeCookTime"
                                                   class="form-control" type="time" id="recipeCookTime" min="00:01" required>
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label" for="recipeCreator">Créé par</label>
                                            <input value="<%= recipe.getCreatedBy() %>" name="recipeCreator" type="text"
                                                   class="form-control" id="recipeCreator" placeholder="Nom du créateur"
                                                   required />
                                        </div>
                                        <div class="mb-3">
                                            <label class="form-label" for="recipeCreationDate">Date de création</label>
                                            <input value="<%= recipe.getFormattedCreatedDate() %>" name="recipeCreationDate"
                                                   type="date" class="form-control" id="recipeCreationDate" required />
                                        </div>

                                        <!-- Champ pour le prix -->
                                        <div class="mb-3">
                                            <label class="form-label" for="recipePrice">Prix (€)</label>
                                            <input value="<%= recipe.getPrice() %>" 
                                                   name="recipePrice" 
                                                   type="number" 
                                                   step="0.01" 
                                                   min="0"
                                                   class="form-control" 
                                                   id="recipePrice" 
                                                   required />
                                        </div>

                                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>
