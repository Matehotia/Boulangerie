<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String activeMenuItem = (String) request.getAttribute("activeMenuItem");
%>

<nav class="main-navbar">
    <div class="nav-brand">
        <a href="recipe">
            <img width="30" src="assets/img/favicon/mofo.png" alt="Logo">
            <span>Marmite</span>
        </a>
    </div>

    <ul class="nav-menu">
        <li class="nav-item">
            <a href="recipe" class="nav-link">
                <i class="bx bx-book"></i>
                <span>Recettes</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="ingredient" class="nav-link">
                <i class="bx bx-food-menu"></i>
                <span>Ingrédients</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="category" class="nav-link">
                <i class="bx bx-category"></i>
                <span>Catégories</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="step" class="nav-link">
                <i class="bx bx-list-ol"></i>
                <span>Étapes</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/vente" class="nav-link">
                <i class="bx bx-cart"></i>
                <span>Ventes</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/commission" class="nav-link">
                <i class="bx bx-dollar-circle"></i>
                <span>Commissions</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="review" class="nav-link">
                <i class="bx bx-arrow-back"></i>
                <span>Retours</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="commission-genre" class="nav-link">
                <i class="fas fa-venus-mars"></i>
                <span>Commissions par Genre</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="price-management" class="nav-link ${activeMenuItem == 'price-management' ? 'active' : ''}">
                <i class="bx bx-euro"></i>
                <span>Gestion des prix</span>
            </a>
        </li>
        <li class="nav-item">
            <a href="stock-management" class="nav-link ${activeMenuItem == 'stock-management' ? 'active' : ''}">
                <i class="bx bx-box"></i>
                <span>Gestion des Stocks</span>
            </a>
        </li>
    </ul>
</nav>

<style>
.main-navbar {
    background: #2b2c40;
    padding: 0.5rem 2rem;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1000;
    display: flex;
    align-items: center;
    justify-content: space-between;
    box-shadow: 0 2px 15px rgba(0,0,0,0.1);
}

.nav-brand {
    display: flex;
    align-items: center;
}

.nav-brand a {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: #fff;
    font-size: 1.25rem;
    font-weight: 600;
}

.nav-brand img {
    margin-right: 0.75rem;
}

.nav-menu {
    display: flex;
    list-style: none;
    margin: 0;
    padding: 0;
    gap: 1rem;
}

.nav-item {
    position: relative;
}

.nav-link {
    color: rgba(255,255,255,0.7);
    text-decoration: none;
    padding: 0.75rem 1rem;
    border-radius: 0.375rem;
    display: flex;
    align-items: center;
    gap: 0.5rem;
    transition: all 0.2s ease;
}

.nav-link:hover {
    color: #fff;
    background: rgba(255,255,255,0.1);
}

.nav-link.active {
    color: #fff;
    background: #696cff;
}

.nav-link i {
    font-size: 1.25rem;
}

/* Responsive */
@media (max-width: 768px) {
    .main-navbar {
        padding: 0.5rem 1rem;
    }

    .nav-menu {
        position: fixed;
        top: 60px;
        left: -100%;
        width: 100%;
        background: #2b2c40;
        flex-direction: column;
        padding: 1rem;
        transition: 0.3s;
    }

    .nav-menu.active {
        left: 0;
    }
}
</style>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname.split('/').pop();
    document.querySelectorAll('.nav-link').forEach(link => {
        if (link.getAttribute('href').includes(currentPath)) {
            link.classList.add('active');
        }
    });
});
</script>


