package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CommissionViewDAO {

    // Attributs représentant les colonnes de la vue "voir_comissions"
    private int id;
    private String dateVente;
    private String nomVendeur;
    private int nombreVentes;
    private BigDecimal montantTotal;
    private BigDecimal montantCommission;
    private BigDecimal pourcentageCommission;

    // Constructeur par défaut
    public CommissionViewDAO() {}

    // Constructeur avec paramètres
    public CommissionViewDAO(int id, String dateVente, String nomVendeur, int nombreVentes, BigDecimal montantTotal, BigDecimal montantCommission) {
        this.id = id;
        this.dateVente = dateVente;
        this.nomVendeur = nomVendeur;
        this.nombreVentes = nombreVentes;
        this.montantTotal = montantTotal;
        this.montantCommission = montantCommission;
    }

    // Méthode statique pour récupérer toutes les commissions depuis la vue
    public static List<CommissionViewDAO> getAll(String startDate, String endDate) throws Exception {
    List<CommissionViewDAO> commissions = new ArrayList<>();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
        connection = DBConnection.getPostgesConnection();
        String query = "SELECT " +
            "vc.id_commission as id, " +
            "vc.vente_date as date_vente, " +
            "u.firstname as nom_vendeur, " +
            "(SELECT COUNT(*) FROM vente_details vd WHERE vd.id_vente = vc.id_vente) as nombre_ventes, " +
            "vc.total_amount as montant_total, " +
            "vc.pourcentage_commission, " +
            "vc.montant_commission " +
            "FROM voir_comissions vc " +
            "JOIN boulangerie_user u ON vc.id_vendeur = u.id_user " +
            "WHERE vc.vente_date >= ?::date " +  // Filtre par date de début
            "AND vc.vente_date <= ?::date + interval '1 day' " +  // Filtre par date de fin
            "ORDER BY vc.vente_date DESC";

        statement = connection.prepareStatement(query);
        statement.setString(1, startDate);  // Définir la date de début
        statement.setString(2, endDate);    // Définir la date de fin

        resultSet = statement.executeQuery();

        while (resultSet.next()) {
            CommissionViewDAO commission = new CommissionViewDAO();
            commission.setId(resultSet.getInt("id"));
            commission.setDateVente(resultSet.getString("date_vente"));
            commission.setNomVendeur(resultSet.getString("nom_vendeur"));
            commission.setNombreVentes(resultSet.getInt("nombre_ventes"));
            commission.setMontantTotal(resultSet.getBigDecimal("montant_total"));
            commission.setPourcentageCommission(resultSet.getBigDecimal("pourcentage_commission"));
            commission.setMontantCommission(resultSet.getBigDecimal("montant_commission"));
            commissions.add(commission);
        }
    } finally {
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        if (connection != null) connection.close();
    }

    return commissions;
}
    // Méthode pour rechercher des commissions par vendeur
    public static List<CommissionViewDAO> getByVendeur(int vendeurId) throws Exception {
        List<CommissionViewDAO> commissions = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Connexion à la base de données
            connection = DBConnection.getPostgesConnection();
            String query = "SELECT * FROM voir_comissions WHERE id_vendeur = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, vendeurId);
            resultSet = statement.executeQuery();

            // Parcourir les résultats
            while (resultSet.next()) {
                commissions.add(new CommissionViewDAO(
                        resultSet.getInt("id_commission"),
                        resultSet.getString("date_vente"),
                        resultSet.getString("nom_vendeur"),
                        resultSet.getInt("nombre_ventes"),
                        resultSet.getBigDecimal("montant_total"),
                        resultSet.getBigDecimal("montant_commission")
                ));
            }
        } finally {
            // Fermeture des ressources
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return commissions;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getDateVente() {
        return dateVente;
    }

    public String getNomVendeur() {
        return nomVendeur;
    }

    public int getNombreVentes() {
        return nombreVentes;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public BigDecimal getMontantCommission() {
        return montantCommission;
    }

    public BigDecimal getPourcentageCommission() {
        return pourcentageCommission;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDateVente(String dateVente) {
        this.dateVente = dateVente;
    }

    public void setNomVendeur(String nomVendeur) {
        this.nomVendeur = nomVendeur;
    }

    public void setNombreVentes(int nombreVentes) {
        this.nombreVentes = nombreVentes;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public void setMontantCommission(BigDecimal montantCommission) {
        this.montantCommission = montantCommission;
    }

    public void setPourcentageCommission(BigDecimal pourcentageCommission) {
        this.pourcentageCommission = pourcentageCommission;
    }

    @Override
    public String toString() {
        return "CommissionViewDAO{" +
                "id=" + id +
                ", dateVente='" + dateVente + '\'' +
                ", nomVendeur='" + nomVendeur + '\'' +
                ", nombreVentes=" + nombreVentes +
                ", montantTotal=" + montantTotal +
                ", montantCommission=" + montantCommission +
                ", pourcentageCommission=" + pourcentageCommission +
                '}';
    }

    public static CommissionViewDAO getById(int id) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        CommissionViewDAO commission = null;

        try {
            connection = DBConnection.getPostgesConnection();
            String query = "SELECT " +
                "vc.id_commission as id, " +
                "vc.vente_date as date_vente, " +
                "u.firstname as nom_vendeur, " +
                "vc.total_amount as montant_total, " +
                "vc.pourcentage_commission, " +
                "vc.montant_commission " +
                "FROM voir_comissions vc " +
                "JOIN boulangerie_user u ON vc.id_vendeur = u.id_user " +
                "WHERE vc.id_commission = ?";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                commission = new CommissionViewDAO();
                commission.setId(resultSet.getInt("id"));
                commission.setDateVente(resultSet.getString("date_vente"));
                commission.setNomVendeur(resultSet.getString("nom_vendeur"));
                commission.setMontantTotal(resultSet.getBigDecimal("montant_total"));
                commission.setPourcentageCommission(resultSet.getBigDecimal("pourcentage_commission"));
                commission.setMontantCommission(resultSet.getBigDecimal("montant_commission"));
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return commission;
    }
}