<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="dao.Ingredient, dao.User,dao.Category, java.util.ArrayList, util.SessionUtils, dao.Vente" %>
<%
    boolean connected = SessionUtils.isUserConnected(request);
    String errorMessage = (String) request.getAttribute("errorMessage");
    ArrayList<User> clients = (ArrayList<User>) request.getAttribute("clients");
%>

<%@ include file="header.jsp" %> <!-- Header section -->
<%@ include file="vertical-menu.jsp" %>

<div class="content-wrapper">
    <div class="container-fluid">
        <!-- En-tête -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card bg-gradient-success text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h3 class="card-title text-white mb-2">Nouvelle Vente</h3>
                                <p class="mb-0">Enregistrer une nouvelle vente</p>
                            </div>
                            <i class='bx bx-cart-add' style="font-size: 3rem; opacity: 0.5"></i>
                        </div>
                </div>
                </div>
                        </div>
                        </div>

        <!-- Formulaire de vente -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <form id="venteForm" method="post" action="vente">
                            <!-- Sélection du client -->
                            <div class="mb-3">
                                <label for="client" class="form-label">Client</label>
                                <select class="form-select" id="client" name="client" required>
                                    <option value="">Sélectionner un client</option>
                                    <% if(clients != null) { %>
                                        <% for(User client : clients) { %>
                                            <option value="<%= client.getId() %>">
                                                <%= client.getFullName() %>
                                            </option>
                                        <% } %>
                                    <% } %>
                                </select>
                            </div>

                            <!-- Date de vente -->
                            <div class="mb-3">
                                <label for="venteDate" class="form-label">Date de vente</label>
                                <input type="datetime-local" class="form-control" id="venteDate" name="venteDate" 
                                       value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new java.util.Date()) %>" 
                                       required>
                            </div>

                            <!-- Liste des produits -->
                            <div class="products-container mb-4">
                                <h5 class="mb-3">Produits</h5>
                                <div class="table-responsive">
                                    <table class="table table-hover" id="productsTable">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Produit</th>
                                                <th style="width: 150px;">Quantité</th>
                                                <th style="width: 150px;">Prix unitaire</th>
                                                <th style="width: 150px;">Total</th>
                                                <th style="width: 50px;"></th>
                                    </tr>
                                </thead>
                                <tbody>
                                            <tr class="product-row">
                                                <td>
                                                    <select class="form-select product-select" name="products[]" required>
                                                        <option value="">Choisir un produit</option>
                                                        <% 
                                                        ArrayList<String[]> recipes = (ArrayList<String[]>) request.getAttribute("recipes");
                                                        if(recipes != null) {
                                                            for(String[] recipe : recipes) { 
                                                        %>
                                                            <option value="<%= recipe[0] %>">
                                                                <%= recipe[1] %>
                                                            </option>
                                <% 
                                        }
                                    }
                                %>
                                                    </select>
                                                </td>
                                                <td>
                                                    <input type="number" class="form-control quantity" name="quantities[]" min="1" value="1" required>
                                                </td>
                                                <td>
                                                    <input type="number" class="form-control price" name="prices[]" min="0" step="0.01" required>
                                                </td>
                                                <td>
                                                    <input type="number" class="form-control total" readonly>
                                                </td>
                                                <td>
                                                    <button type="button" class="btn btn-icon btn-outline-danger remove-product">
                                                        <i class="bx bx-trash"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                </tbody>
                            </table>
                        </div>
                                <button type="button" class="btn btn-outline-primary mt-3" id="addProduct">
                                    <i class="bx bx-plus me-1"></i> Ajouter un produit
                                </button>
                            </div>

                            <!-- Total et validation -->
                            <div class="row justify-content-end">
                                <div class="col-md-4">
                                    <div class="card bg-light">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between border-top pt-2">
                                                <span class="fw-bold">Total :</span>
                                                <span id="total" class="fw-bold text-primary">0.00 Ar</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="text-end mt-4">
                                <button type="button" class="btn btn-outline-secondary me-2" onclick="history.back()">
                                    <i class="bx bx-arrow-back me-1"></i> Retour
                                </button>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bx bx-check me-1"></i> Valider la vente
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
/* Styles pour vente.jsp */
.bg-gradient-success {
    background: linear-gradient(135deg, #71dd37 0%, #36b9cc 100%);
}

.card {
    transition: transform 0.2s ease, box-shadow 0.2s ease;
    border: none;
    box-shadow: 0 2px 6px rgba(67, 89, 113, 0.12);
}

.product-row {
    transition: all 0.3s ease;
}

.product-row:hover {
    background-color: rgba(67, 89, 113, 0.05);
}

.btn-icon {
    width: 32px;
    height: 32px;
    padding: 0;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
}

.form-select, .form-control {
    border-radius: 0.375rem;
    border: 1px solid var(--border-color);
    padding: 0.5rem 0.875rem;
    transition: all 0.2s ease;
}

.form-select:focus, .form-control:focus {
    border-color: var(--primary-color);
    box-shadow: 0 0 0.25rem rgba(105, 108, 255, 0.1);
}

/* Animation pour l'ajout de produit */
@keyframes slideDown {
    from {
        opacity: 0;
        transform: translateY(-10px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.product-row.new {
    animation: slideDown 0.3s ease-out;
}

/* Responsive design */
@media (max-width: 768px) {
    .table-responsive {
        margin: 0 -1rem;
    }
    
    .btn-icon {
        width: 28px;
        height: 28px;
    }
}
</style>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // Fonction pour calculer les totaux
    function calculateTotals() {
        let subtotal = 0;
        document.querySelectorAll('.product-row').forEach(row => {
            const quantity = parseFloat(row.querySelector('.quantity').value) || 0;
            const price = parseFloat(row.querySelector('.price').value) || 0;
            const total = quantity * price;
            row.querySelector('.total').value = total.toFixed(2);
            subtotal += total;
        });

        document.getElementById('total').textContent = subtotal.toFixed(2) + ' Ar';
    }

    // Gérer le changement de produit
    function handleProductChange(select) {
        const row = select.closest('.product-row');
        const option = select.selectedOptions[0];
        const price = option.dataset.price || 0;
        
        row.querySelector('.price').value = price;
        calculateTotals();
    }

    // Ajouter l'événement aux selects existants
    document.querySelectorAll('.product-select').forEach(select => {
        select.addEventListener('change', function() {
            handleProductChange(this);
        });
    });

    // Modifier la fonction d'ajout de produit
    document.getElementById('addProduct').addEventListener('click', function() {
        const template = document.querySelector('.product-row').cloneNode(true);
        template.classList.add('new');
        
        // Copier les options de la liste déroulante originale
        const originalSelect = document.querySelector('.product-select');
        const newSelect = template.querySelector('.product-select');
        newSelect.innerHTML = originalSelect.innerHTML;
        newSelect.value = '';
        
        // Réinitialiser les autres champs
        template.querySelectorAll('input').forEach(input => input.value = '');
        
        document.querySelector('#productsTable tbody').appendChild(template);
        
        // Ajouter les événements
        addRowEvents(template);
        
        // Ajouter l'événement de changement de produit
        newSelect.addEventListener('change', function() {
            handleProductChange(this);
        });
    });

    // Fonction pour ajouter les événements à une ligne
    function addRowEvents(row) {
        row.querySelector('.remove-product').addEventListener('click', function() {
            row.remove();
            calculateTotals();
        });

        row.querySelector('.quantity').addEventListener('input', calculateTotals);
        row.querySelector('.price').addEventListener('input', calculateTotals);
    }

    // Initialiser les événements pour les lignes existantes
    document.querySelectorAll('.product-row').forEach(addRowEvents);
});
</script>

<!-- Ajouter ceci après le titre -->
<% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger alert-dismissible" role="alert">
        <%= request.getAttribute("error") %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
<% } %>
</body>
</html>