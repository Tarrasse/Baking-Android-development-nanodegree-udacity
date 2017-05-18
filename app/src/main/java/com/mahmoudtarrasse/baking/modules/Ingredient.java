package com.mahmoudtarrasse.baking.modules;

/**
 * Created by mahmoud on 09/05/17.
 */

public class Ingredient {

    /**
     * quantity : 2
     * measure : CUP
     * ingredient : GrahamCrackercrumbs
     */

    private float quantity;
    private String measure;
    private String ingredient;


    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getQuantity(){
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
