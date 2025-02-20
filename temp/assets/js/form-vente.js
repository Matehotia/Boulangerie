
function addProductForm() {
    const productsTableBody = document.getElementById('productsTableBody');
    
    // Create a new table row for the product
    const newRow = document.createElement('tr');
    
    newRow.innerHTML = `
        <td>
            <select name="recipeId[]" class="form-control" required onchange="setUnitPrice(this)">
                <option value="" disabled selected>Select Recipe</option>
                <% 
                for (Recipe recipe : recipes) { 
                    String recipeId = recipe.getId();
                    String recipeTitle = recipe.getTitle();
                    double recipePrice = recipe.getPrice();
                %>
                    <option value="<%= recipeId %>" data-price="<%= recipePrice %>">
                        <%= recipeTitle %>
                    </option>
                <% } %>
            </select>
        </td>
        <td>
            <input type="number" name="unitPrice[]" class="form-control" step="0.01" placeholder="Prix Unitaire" readonly />
        </td>
        <td>
            <input type="number" name="quantity[]" class="form-control" placeholder="Quantité" required />
        </td>
        <td>
            <input type="number" name="subtotal[]" class="form-control" step="0.01" placeholder="Subtotal" readonly />
        </td>
        <td>
            <button type="button" class="btn btn-danger" onclick="removeProductRow(this)">Supprimer</button>
        </td>
    `;
    
    productsTableBody.appendChild(newRow);

    // Update the subtotal whenever quantity or unit price changes
    updateSubTotal();
}


// Function to set the unit price based on the selected recipe
function setUnitPrice(selectElement) {
    const unitPriceInput = selectElement.closest('tr').querySelector('[name="unitPrice[]"]');
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const price = selectedOption.getAttribute('data-price');
    
    unitPriceInput.value = price;
    
    // Update the subtotal for the new unit price
    updateSubTotal();
}

// Function to remove a product row
function removeProductRow(button) {
    const row = button.closest('tr');
    row.remove();
}

// Function to update the subtotal dynamically based on quantity and unit price
function updateSubTotal() {
    const rows = document.querySelectorAll('#productsTableBody tr');
    rows.forEach(row => {
        const quantity = row.querySelector('[name="quantity[]"]');
        const unitPrice = row.querySelector('[name="unitPrice[]"]');
        const subtotal = row.querySelector('[name="subtotal[]"]');
        
        if (quantity && unitPrice && subtotal) {
            subtotal.value = (quantity.value * unitPrice.value).toFixed(2);
        }
    });
}

// Add event listeners to dynamically calculate subtotal
document.addEventListener('input', (event) => {
    if (event.target.matches('[name="quantity[]"], [name="unitPrice[]"]')) {
        updateSubTotal();
    }
});