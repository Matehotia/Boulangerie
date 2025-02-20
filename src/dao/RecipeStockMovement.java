package dao;

import java.time.LocalDateTime;

public class RecipeStockMovement {
    private int idMovement;
    private int idRecipe;
    private String movementType;
    private int quantity;
    private LocalDateTime movementDate;
    private String reason;
    private String modifiedBy;
    private String recipeName;

    // Constructeurs
    public RecipeStockMovement() {}

    public RecipeStockMovement(int idRecipe, String movementType, int quantity, String reason, String modifiedBy) {
        this.idRecipe = idRecipe;
        this.movementType = movementType;
        this.quantity = quantity;
        this.reason = reason;
        this.modifiedBy = modifiedBy;
        this.movementDate = LocalDateTime.now();
    }

    // Getters et Setters
    public int getIdMovement() {
        return idMovement;
    }

    public void setIdMovement(int idMovement) {
        this.idMovement = idMovement;
    }

    public int getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(LocalDateTime movementDate) {
        this.movementDate = movementDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
}