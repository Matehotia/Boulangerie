<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="dao.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Détails Commission</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>

<div class="container mt-4">
    <div class="row mb-3">
        <div class="col">
            <h2>Détails de la Commission</h2>
            <a href="commission" class="btn btn-secondary mb-3">
                <i class="bi bi-arrow-left"></i> Retour
            </a>
        </div>
    </div>

    <% CommissionViewDAO commission = (CommissionViewDAO) request.getAttribute("commission"); %>
    
    <div class="card mb-4">
        <div class="card-header">
            <h5>Informations Générales</h5>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-3">
                    <p><strong>Vendeur:</strong> <%= commission.getNomVendeur() %></p>
                </div>
                <div class="col-md-3">
                    <p><strong>Date:</strong> <%= commission.getDateVente() %></p>
                </div>
                <div class="col-md-3">
                    <p><strong>Nombre de ventes:</strong> <%= commission.getNombreVentes() %></p>
                </div>
                <div class="col-md-3">
                    <p><strong>Commission:</strong> <%= commission.getPourcentageCommission() %>%</p>
                </div>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-header">
            <h5>Détails des Ventes</h5>
        </div>
        <div class="card-body">
            <table class="table">
                <thead>
                    <tr>
                        <th>Produit</th>
                        <th>Quantité</th>
                        <th>Prix unitaire</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    ArrayList<VenteDetails> details = (ArrayList<VenteDetails>) request.getAttribute("details");
                    if(details != null) {
                        for(VenteDetails detail : details) {
                    %>
                    <tr>
                        <td><%= detail.getRecipeName() %></td>
                        <td><%= detail.getQuantity() %></td>
                        <td><%= detail.getUnitPrice() %></td>
                        <td><%= detail.getQuantity() * detail.getUnitPrice() %></td>
                    </tr>
                    <% 
                        }
                    } 
                    %>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="3" class="text-end"><strong>Total des ventes:</strong></td>
                        <td><%= commission.getMontantTotal() %></td>
                    </tr>
                    <tr>
                        <td colspan="3" class="text-end"><strong>Montant commission:</strong></td>
                        <td><%= commission.getMontantCommission() %></td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>