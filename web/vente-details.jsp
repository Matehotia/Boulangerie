<style>
/* Main Container */
.content-wrapper {
    background: #f8f9fa;
    min-height: 100vh;
    padding: 2rem;
}

/* Sale Details Header */
.sale-header {
    background: linear-gradient(135deg, #696cff 0%, #8592d6 100%);
    border-radius: 1rem;
    padding: 2rem;
    margin-bottom: 2rem;
    color: white;
    position: relative;
    overflow: hidden;
}

.sale-header::after {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 300px;
    height: 100%;
    background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1));
}

.sale-title {
    font-size: 1.75rem;
    font-weight: 600;
    margin-bottom: 0.5rem;
}

.sale-info {
    display: flex;
    gap: 2rem;
    margin-top: 1rem;
}

.info-item {
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.info-item i {
    font-size: 1.25rem;
    opacity: 0.8;
}

/* Summary Card */
.summary-card {
    background: white;
    border-radius: 1rem;
    padding: 1.5rem;
    margin-bottom: 2rem;
    box-shadow: 0 2px 6px rgba(67, 89, 113, 0.12);
    transition: transform 0.3s ease;
}

.summary-card:hover {
    transform: translateY(-3px);
    box-shadow: 0 4px 12px rgba(67, 89, 113, 0.16);
}

.summary-title {
    color: #566a7f;
    font-size: 1.1rem;
    font-weight: 600;
    margin-bottom: 1.5rem;
    display: flex;
    align-items: center;
    gap: 0.5rem;
}

.summary-title i {
    color: #696cff;
}

/* Details Table */
.details-table {
    background: white;
    border-radius: 1rem;
    overflow: hidden;
    box-shadow: 0 2px 6px rgba(67, 89, 113, 0.12);
}

.table {
    margin-bottom: 0;
}

.table thead th {
    background: #f8f9fa;
    color: #566a7f;
    font-weight: 600;
    text-transform: uppercase;
    font-size: 0.75rem;
    letter-spacing: 0.5px;
    padding: 1rem;
    border-bottom: 2px solid #eceef1;
}

.table tbody td {
    padding: 1.25rem 1rem;
    vertical-align: middle;
    border-bottom: 1px solid #eceef1;
}

.table tbody tr {
    transition: all 0.2s ease;
}

.table tbody tr:hover {
    background-color: rgba(105, 108, 255, 0.04);
    transform: scale(1.005);
}

/* Product Column */
.product-info {
    display: flex;
    align-items: center;
    gap: 1rem;
}

.product-image {
    width: 48px;
    height: 48px;
    border-radius: 0.5rem;
    background: #f5f5f9;
    display: flex;
    align-items: center;
    justify-content: center;
}

.product-image i {
    color: #696cff;
    font-size: 1.5rem;
}

.product-details {
    flex: 1;
}

.product-name {
    font-weight: 600;
    color: #566a7f;
    margin-bottom: 0.25rem;
}

.product-category {
    font-size: 0.875rem;
    color: #697a8d;
}

/* Price Formatting */
.price {
    font-family: 'Roboto Mono', monospace;
    font-weight: 600;
    color: #566a7f;
}

.total-price {
    font-size: 1.1rem;
    color: #696cff;
}

/* Quantity Badge */
.quantity-badge {
    background: rgba(105, 108, 255, 0.08);
    color: #696cff;
    padding: 0.5em 1em;
    border-radius: 0.375rem;
    font-weight: 600;
}

/* Status Indicators */
.status-indicator {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    display: inline-block;
    margin-right: 0.5rem;
}

.status-success {
    background-color: #71dd37;
}

.status-pending {
    background-color: #ffab00;
}

/* Action Buttons */
.btn-action {
    padding: 0.5rem;
    border-radius: 0.375rem;
    transition: all 0.2s ease;
}

.btn-action:hover {
    transform: translateY(-2px);
}

.btn-print {
    background: rgba(105, 108, 255, 0.08);
    color: #696cff;
}

.btn-print:hover {
    background: #696cff;
    color: white;
}

/* Totals Section */
.totals-section {
    background: white;
    border-radius: 1rem;
    padding: 1.5rem;
    margin-top: 2rem;
    box-shadow: 0 2px 6px rgba(67, 89, 113, 0.12);
}

.total-row {
    display: flex;
    justify-content: space-between;
    padding: 0.75rem 0;
    border-bottom: 1px solid #eceef1;
}

.total-row:last-child {
    border-bottom: none;
    padding-bottom: 0;
}

.total-label {
    color: #697a8d;
}

.grand-total {
    font-size: 1.25rem;
    font-weight: 600;
    color: #696cff;
}

/* Responsive Design */
@media (max-width: 768px) {
    .content-wrapper {
        padding: 1rem;
    }

    .sale-header {
        padding: 1.5rem;
    }

    .sale-info {
        flex-direction: column;
        gap: 1rem;
    }

    .table-responsive {
        margin: 0 -1rem;
    }

    .product-info {
        flex-direction: column;
        align-items: flex-start;
        gap: 0.5rem;
    }
}

/* Animations */
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.summary-card, .details-table {
    animation: fadeInUp 0.4s ease-out forwards;
}

/* Print Styles */
@media print {
    .content-wrapper {
        padding: 0;
        background: white;
    }

    .sale-header {
        background: none;
        color: #000;
        padding: 1rem 0;
    }

    .btn-action {
        display: none;
    }
}
</style> 