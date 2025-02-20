document.addEventListener('DOMContentLoaded', function() {
    // Initialiser le calcul du total au chargement
    updateTotal();
});

function addProductForm() {
    const productsTableBody = document.getElementById('productsTableBody');
    const template = document.getElementById('product-row-template').innerHTML;
    
    // Create a new row dynamically
    const newRow = document.createElement('tr');
    newRow.innerHTML = template;
    
    productsTableBody.appendChild(newRow);
}

function removeProductRow(button) {
    const row = button.closest('tr');
    row.remove();
    updateTotal();
}

function setUnitPrice(selectElement) {
    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const price = selectedOption.getAttribute('data-price');
    const row = selectElement.closest('tr');
    const unitPriceInput = row.querySelector('input[name="unitPrice[]"]');
    unitPriceInput.value = price;
    calculateSubtotal(row);
}

function calculateSubtotal(row) {
    const unitPriceInput = row.querySelector('input[name="unitPrice[]"]');
    const quantityInput = row.querySelector('input[name="quantity[]"]');
    const subtotalInput = row.querySelector('input[name="subtotal[]"]');
    
    const unitPrice = parseFloat(unitPriceInput.value) || 0;
    const quantity = parseInt(quantityInput.value) || 0;

    const subtotal = unitPrice * quantity;
    subtotalInput.value = subtotal.toFixed(2);

    updateTotal();
}

function updateTotal() {
    const subtotalInputs = document.querySelectorAll('input[name="subtotal[]"]');
    const totalInput = document.getElementById('totalAmount');

    let total = 0;
    subtotalInputs.forEach(input => {
        total += parseFloat(input.value) || 0;
    });

    totalInput.value = total.toFixed(2);
} 