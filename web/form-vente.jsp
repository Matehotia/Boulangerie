<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="dao.Recipe, dao.Category, java.util.ArrayList, util.SessionUtils,dao.User"%>

<%
    boolean connected = SessionUtils.isUserConnected(request);
    User u = null;
    if(connected){
        u=SessionUtils.getConnectedUser(request);
    }
    ArrayList<Recipe> recipes = (ArrayList<Recipe>) request.getAttribute("recipes");
%>

<%@ include file="header.jsp" %>
<div class="layout-wrapper layout-content-navbar">
    <div class="layout-container">
        <%@ include file="vertical-menu.jsp" %>
        <div class="layout-page">
            <nav class="layout-navbar container-xxl navbar navbar-expand-xl navbar-detached align-items-center bg-navbar-theme" id="layout-navbar">
                <div class="layout-menu-toggle navbar-nav align-items-xl-center me-3 me-xl-0 d-xl-none">
                    <a class="nav-item nav-link px-0 me-xl-4" href="javascript:void(0)">
                        <i class="bx bx-menu bx-sm"></i>
                    </a>
                </div>
                <div class="navbar-nav-right d-flex align-items-center" id="navbar-collapse">
                    <ul class="navbar-nav flex-row align-items-center ms-auto">
                        <%@ include file="user.jsp" %>
                    </ul>
                </div>
            </nav>
            <div class="content-wrapper">
                <div class="container-xxl flex-grow-1 container-p-y">
                    <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Formulaire /</span> Vente</h4>
                    <div class="row">
                        <div class="col-lg-8 mx-auto">
                            <div class="card mb-4">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">Vente</h5>
                                </div>
                                <div class="card-body">
                                    <form action="formvente" method="post">
                                        <!-- Ajout du sélecteur de client -->
                                        <div class="mb-3">
                                            <label class="form-label">Client</label>
                                            <select name="clientId" class="form-select" required>
                                                <option value="" disabled selected>Sélectionner un client</option>
                                                <% for (User client : (ArrayList<User>) request.getAttribute("clients")) { %>
                                                    <option value="<%= client.getId() %>">
                                                        <%= client.getFirstname() %> <%= client.getLastname() %>
                                                    </option>
                                                <% } %>
                                            </select>
                                        </div>

                                        <div class="mb-3">
                                            <label for="saleDate" class="form-label">Date de vente</label>
                                            <input type="date" id="saleDate" name="date" class="form-control" required>
                                        </div>
                                        <div class="mb-3">
                                            <label for="totalAmount" class="form-label">Montant total</label>
                                            <input type="number" step="0.01" id="totalAmount" name="totalAmount" class="form-control" required>
                                        </div>

                                        <!-- Table for dynamic products -->
                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <th>Recette</th>
                                                    <th>Prix Unitaire</th>
                                                    <th>Quantité</th>
                                                    <th>Sous-total</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody id="productsTableBody">
                                                <!-- Initial Row -->
                                                <tr>
                                                    <td>
                                                        <select name="recipeId[]" class="form-control" required onchange="setUnitPrice(this)">
                                                            <option value="" disabled selected>Select Recipe</option>
                                                            <% for (Recipe recipe : recipes) { %>
                                                                <option value="<%= recipe.getId() %>" data-price="<%= recipe.getPrice() %>">
                                                                    <%= recipe.getTitle() %>
                                                                </option>
                                                            <% } %>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <input type="number" name="unitPrice[]" class="form-control" step="0.01" placeholder="Prix Unitaire" readonly />
                                                    </td>
                                                    <td>
                                                        <input type="number" name="quantity[]" class="form-control" placeholder="Quantité" required oninput="calculateSubtotal(this.closest('tr'))" />
                                                    </td>
                                                    <td>
                                                        <input type="number" name="subtotal[]" class="form-control" step="0.01" placeholder="Sous-total" readonly />
                                                    </td>
                                                    <td>
                                                        <button type="button" class="btn btn-danger" onclick="removeProductRow(this)">Supprimer</button>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>

                                        <!-- Button to add new rows -->
                                        <button type="button" class="btn btn-primary mt-3" onclick="addProductForm()">Ajouter une ligne</button>

                                        <!-- Submit button -->
                                        <button type="submit" class="btn btn-success mt-3">Enregistrer la vente</button>
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

<script>
    function addProductForm() {
        const productsTableBody = document.getElementById('productsTableBody');
        
        // Create a new row dynamically
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>
                <select name="recipeId[]" class="form-control" required onchange="setUnitPrice(this)">
                    <option value="" disabled selected>Select Recipe</option>
                    <% for (Recipe recipe : recipes) { %>
                        <option value="<%= recipe.getId() %>" data-price="<%= recipe.getPrice() %>">
                            <%= recipe.getTitle() %>
                        </option>
                    <% } %>
                </select>
            </td>
            <td>
                <input type="number" name="unitPrice[]" class="form-control" step="0.01" placeholder="Prix Unitaire" readonly />
            </td>
            <td>
                <input type="number" name="quantity[]" class="form-control" placeholder="Quantité" required oninput="calculateSubtotal(this.closest('tr'))"/>
            </td>
            <td>
                <input type="number" name="subtotal[]" class="form-control" step="0.01" placeholder="Sous-total" readonly />
            </td>
            <td>
                <button type="button" class="btn btn-danger" onclick="removeProductRow(this)">Supprimer</button>
            </td>
        `;
        
        productsTableBody.appendChild(newRow);
    }

    function removeProductRow(button) {
        const row = button.parentElement.parentElement;
        row.remove();
    }

    function setUnitPrice(selectElement) {
        const selectedOption = selectElement.options[selectElement.selectedIndex];
        const price = selectedOption.getAttribute('data-price');
        const unitPriceInput = selectElement.closest('tr').querySelector('input[name="unitPrice[]"]');
        unitPriceInput.value = price;
    }
    function calculateSubtotal(row) {
    const unitPriceInput = row.querySelector('input[name="unitPrice[]"]');
    const quantityInput = row.querySelector('input[name="quantity[]"]');
    const subtotalInput = row.querySelector('input[name="subtotal[]"]');
    
    const unitPrice = parseFloat(unitPriceInput.value) || 0;
    const quantity = parseFloat(quantityInput.value) || 0;

    const subtotal = unitPrice * quantity;
    subtotalInput.value = subtotal.toFixed(2); // Fixe le sous-total avec deux décimales

    updateTotal(); // Mets à jour le total général
}
function updateTotal() {
    const subtotalInputs = document.querySelectorAll('input[name="subtotal[]"]');
    const totalInput = document.getElementById('totalAmount');

    let total = 0;
    subtotalInputs.forEach(input => {
        total += parseFloat(input.value) || 0; // Additionne chaque sous-total
    });

    totalInput.value = total.toFixed(2); // Fixe le total avec deux décimales
}

</script>

<%@include file="footer.jsp"%>