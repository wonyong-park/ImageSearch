package com.sungkyul.imagesearch.es;

import java.util.List;

public interface ITouristManager {

    List<Tourist> searchTourists(String searchString, String field);
    Tourist getTourist(String keyword);

}
