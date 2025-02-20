package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CommissionGenreDAO {
    private String genre;
    private BigDecimal totalCommission;
    private int nombreVendeurs;
    private String periode;

    private String nomVendeur;
    private BigDecimal prixVente;
    private BigDecimal tauxCommission;
    private BigDecimal commission;
    private String dateVente;

    // Constructeur par défaut
    public CommissionGenreDAO() {}

    // Constructeur avec paramètres
    public CommissionGenreDAO(String genre, BigDecimal totalCommission, int nombreVendeurs, String periode) {
        this.genre = genre;
        this.totalCommission = totalCommission;
        this.nombreVendeurs = nombreVendeurs;
        this.periode = periode;
    }
    
    public static Map<String, List<CommissionGenreDAO>> getDetailsVentesByGenre(String genre, String dateDebut, String dateFin) throws Exception {
        Map<String, List<CommissionGenreDAO>> detailsVentesParVendeur = new HashMap<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getPostgesConnection();
            String query = "SELECT " +
                "u.firstname AS nom_vendeur, " +
                "v.total_amount AS prix_vente, " +
                "c.valuee AS taux_commission, " +
                "(v.total_amount * c.valuee / 100) AS commission, " +
                "v.vente_date AS date_vente " +
                "FROM boulangerie_user u " +
                "JOIN vente v ON u.id_user = v.id_user " +
                "JOIN commission c ON u.id_user = c.id_vendeur " +
                "WHERE u.genre = ? " +
                "AND v.vente_date >= ?::date " +
                "AND v.vente_date <= ?::date + interval '1 day' " +
                "AND v.vente_date BETWEEN c.date_debut AND c.date_fin " +
                "ORDER BY v.vente_date";

            statement = connection.prepareStatement(query);
            statement.setString(1, genre);
            statement.setString(2, dateDebut);
            statement.setString(3, dateFin);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CommissionGenreDAO detail = new CommissionGenreDAO();
                detail.setNomVendeur(resultSet.getString("nom_vendeur"));
                detail.setPrixVente(resultSet.getBigDecimal("prix_vente"));
                detail.setTauxCommission(resultSet.getBigDecimal("taux_commission"));
                detail.setCommission(resultSet.getBigDecimal("commission"));
                detail.setDateVente(resultSet.getString("date_vente"));

                String nomVendeur = detail.getNomVendeur();
                detailsVentesParVendeur.computeIfAbsent(nomVendeur, k -> new ArrayList<>()).add(detail);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return detailsVentesParVendeur;
    }

    public static List<CommissionGenreDAO> getCommissionsByPeriode(String dateDebut, String dateFin) throws Exception {
        List<CommissionGenreDAO> commissions = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnection.getPostgesConnection();
            String query = "SELECT " +
                "CASE " +
                "    WHEN u.genre = 'F' THEN 'Femmes' " +
                "    WHEN u.genre = 'M' THEN 'Hommes' " +
                "END as Genre, " +
                "SUM(v.total_amount * c.valuee / 100) as total_commission, " +
                "COUNT(DISTINCT u.id_user) as nombre_vendeurs, " +
                "TO_CHAR(v.vente_date, 'YYYY-MM') as periode " +
                "FROM boulangerie_user u " +
                "JOIN vente v ON u.id_user = v.id_user " +
                "JOIN commission c ON u.id_user = c.id_vendeur " +
                "WHERE v.vente_date >= ?::date " +
                "AND v.vente_date <= ?::date + interval '1 day' " +
                "AND v.vente_date BETWEEN c.date_debut AND c.date_fin " +
                "GROUP BY u.genre, TO_CHAR(v.vente_date, 'YYYY-MM') " +
                "ORDER BY periode, Genre";

            statement = connection.prepareStatement(query);
            statement.setString(1, dateDebut);
            statement.setString(2, dateFin);

            // Debug
            System.out.println("Date début: " + dateDebut);
            System.out.println("Date fin: " + dateFin);
            System.out.println("Requête SQL: " + query);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CommissionGenreDAO commission = new CommissionGenreDAO(
                    resultSet.getString("Genre"),
                    resultSet.getBigDecimal("total_commission"),
                    resultSet.getInt("nombre_vendeurs"),
                    resultSet.getString("periode")
                );
                commissions.add(commission);
                
                // Debug
                System.out.println("Commission trouvée: " + commission.getGenre() + 
                                 " - " + commission.getTotalCommission() + 
                                 " - " + commission.getPeriode());
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return commissions;
    }

    // Getters et Setters
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    public int getNombreVendeurs() {
        return nombreVendeurs;
    }

    public void setNombreVendeurs(int nombreVendeurs) {
        this.nombreVendeurs = nombreVendeurs;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }
    // Getters et Setters pour les nouveaux attributs
    public String getNomVendeur() {
        return nomVendeur;
    }

    public void setNomVendeur(String nomVendeur) {
        this.nomVendeur = nomVendeur;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public BigDecimal getTauxCommission() {
        return tauxCommission;
    }

    public void setTauxCommission(BigDecimal tauxCommission) {
        this.tauxCommission = tauxCommission;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getDateVente() {
        return dateVente;
    }

    public void setDateVente(String dateVente) {
        this.dateVente = dateVente;
    }
} 