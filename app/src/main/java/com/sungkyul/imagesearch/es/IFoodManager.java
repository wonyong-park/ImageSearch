package com.sungkyul.imagesearch.es;

import java.util.List;

public interface IFoodManager {

    List<Food> searchFoods(String searchString, String field);
    Food getFood(String keyword);
}
