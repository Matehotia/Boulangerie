<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Marmite - Gestion de Boulangerie</title>

    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="assets/img/favicon/mofo.png">

    <!-- Icons. Uncomment required icon fonts -->
    <link rel="stylesheet" href="assets/vendor/fonts/boxicons.css"/>

    <!-- Core CSS -->
    <link rel="stylesheet" href="assets/vendor/css/core.css" class="template-customizer-core-css"/>
    <link rel="stylesheet" href="assets/vendor/css/theme-default.css" class="template-customizer-theme-css"/>
    <link rel="stylesheet" href="assets/css/demo.css"/>

    <!-- Vendors CSS -->
    <link rel="stylesheet" href="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css"/>

    <link rel="stylesheet" href="assets/vendor/libs/apex-charts/apex-charts.css"/>

    <!-- Page CSS -->
    <link rel="stylesheet" href="assets/css/style.css">

    <!-- Helpers -->
    <script src="assets/vendor/js/helpers.js"></script>

    <!--! Template customizer & Theme config files MUST be included after core stylesheets and helpers.js in the <head> section -->
    <!--? Config:  Mandatory theme config file contain global vars & default theme options, Set your preferred theme option in this file.  -->
    <script src="assets/js/config.js"></script>

    <!-- Custom Style -->
    <style>
    :root {
        --primary-color: #696cff;
        --secondary-color: #8592d6;
        --dark-color: #2b2c40;
        --light-color: #f5f5f9;
        --text-color: #566a7f;
        --border-color: #d9dee3;
    }

    body {
        padding-top: 60px;
        background-color: var(--light-color);
        color: var(--text-color);
        font-family: 'Public Sans', sans-serif;
    }

    .content-wrapper {
        padding: 2rem;
    }

    .card {
        border: none;
        border-radius: 0.5rem;
        box-shadow: 0 2px 6px rgba(67, 89, 113, 0.12);
        margin-bottom: 1.5rem;
        background: white;
    }

    .card-header {
        background: none;
        padding: 1.5rem;
        border-bottom: 1px solid var(--border-color);
    }

    .btn-primary {
        background-color: var(--primary-color);
        border-color: var(--primary-color);
    }

    .btn-primary:hover {
        background-color: var(--secondary-color);
        border-color: var(--secondary-color);
    }

    .table {
        margin-bottom: 0;
    }

    .table th {
        font-weight: 600;
        text-transform: uppercase;
        font-size: 0.75rem;
        letter-spacing: 0.5px;
    }

    .badge {
        padding: 0.5em 0.75em;
        font-weight: 500;
    }

    .form-control {
        border: 1px solid var(--border-color);
        padding: 0.5rem 0.875rem;
    }

    .form-control:focus {
        border-color: var(--primary-color);
        box-shadow: 0 0 0.25rem rgba(105, 108, 255, 0.1);
    }

    /* Responsive Design */
    @media (max-width: 768px) {
        .content-wrapper {
            padding: 1rem;
        }

        .card {
            margin-bottom: 1rem;
        }
    }
    </style>
</head>
<body>
