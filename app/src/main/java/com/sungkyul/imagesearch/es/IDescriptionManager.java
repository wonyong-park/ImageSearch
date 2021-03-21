package com.sungkyul.imagesearch.es;

import java.util.List;

public interface IDescriptionManager {

    List<Description> searchDescription(String searchString, String field);
    Description getDescription(String keyword);
}
