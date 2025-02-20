<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="vertical-menu.jsp" %>
<%@ page import="dao.PriceHistory" %>
<%@ page import="java.util.List" %>


<%@ include file="header.jsp" %>

<h3>Historique des prix</h3>

<table border="1">
    <thead>
        <tr>
            <th>Prix</th>
            <th>Date de début</th>
            <th>Date de fin</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="priceHistory" items="${priceHistoryList}">
            <tr>
                <td>${priceHistory.price}</td>
                <td>${priceHistory.startDate}</td>
                <td>${priceHistory.endDate != null ? priceHistory.endDate : "Actuel"}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="footer.jsp" %>
