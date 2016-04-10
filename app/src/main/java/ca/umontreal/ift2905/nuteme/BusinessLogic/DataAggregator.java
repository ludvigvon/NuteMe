package ca.umontreal.ift2905.nuteme.BusinessLogic;

import java.util.ArrayList;
import java.util.List;

import ca.umontreal.ift2905.nuteme.DataModel.Ingredient;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrient;
import ca.umontreal.ift2905.nuteme.DataModel.Nutrition;
import ca.umontreal.ift2905.nuteme.DataModel.Recipe;

/**
 * Created by h on 25/03/16.
 */
public class DataAggregator {

     /* TODO: ajouter une méthode qui reçoit une liste de recettes fait une agrégation sur les
           nutriments et retourne une liste de nutriments dans une objet Nutrition.
    */
    public Nutrition getSummaryAggregateView(List<Recipe> recipes){
        return new Nutrition();
    }

    /* TODO: ajouter une méthode qui reçoit une liste de recettes fait une agrégation sur les
           ingrédients et retourne une liste d'ingrédients. Pour chaque ingrédient, on veut avoir
           le ratio de chacune des recettes et les principaux éléments nutritionnels
    */
    public List<Ingredient> getAggregateViewByIngredient(List<Recipe> recipes){
        return new ArrayList<Ingredient>();
    }

    /* TODO: ajouter une méthode qui reçoit une liste de recettes fait une agrégation sur les
           nutriments et retourne une liste de nutriments. Pour chaque nutriment, on veut avoir
           les principaux ingrédients qui participents à leur valeur
    */
    public List<Nutrient> getAggregateViewByNutrient(List<Recipe> recipes){
        return new ArrayList<Nutrient>();
    }
}
