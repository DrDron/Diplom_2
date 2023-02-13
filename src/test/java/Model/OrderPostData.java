package Model;

import java.util.List;

public class OrderPostData {
    private List<String> ingredients;

    public OrderPostData(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

}
