// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import javax.servlet.http.HttpServletRequest;
import com.google.sps.data.GivrUser;
import java.io.IOException;

public abstract class ListHelper {

  /* Fills all filtering parameters from servlet request into a hashmap */
  public static HashMap<String, ArrayList<String>> parseFilterParams(String entityKind, HttpServletRequest request, GivrUser currentUser, HashMap<String, String> datastoreConstantMap) {
    /* Stores datastore property name as key, and received filter keywords for said property in arraylist */
    HashMap<String, ArrayList<String>> filterParamMap = new HashMap<String, ArrayList<String>>();

    for (String paramString : new String[]{"name", "streetAddress", "resourceCategories", "zipcode"}) {
      ArrayList<String> paramList = new ArrayList<String>();
      if (request.getParameterValues(paramString) != null) {
        for (String c : request.getParameterValues(paramString)) {
        }
        Collections.addAll(paramList, request.getParameterValues(paramString));
        filterParamMap.put(datastoreConstantMap.get(paramString), paramList);
      }
    }
    return filterParamMap;
  }

  /* This function constructs a query based on the entity kind, request parameters and any previously added or new filters */
  public static Query getQueryFromFilters(String entityKind, HashMap<String, ArrayList<String>> filterParamMap, ArrayList<Filter> filterCollection) throws IllegalArgumentException {
    if ((entityKind == "") || (entityKind == null)) {
      throw new IllegalArgumentException("Entity kind must not be null");
    }
    
    Query query = new Query(entityKind).addSort("creationTimeStampMillis", SortDirection.DESCENDING);

    /* Adds a filter for each keyword in each arraylist of the map, according to its datastore property */
    for (Map.Entry<String, ArrayList<String>> entry : filterParamMap.entrySet()) {
      for (String filterParam : entry.getValue()) {
        filterCollection.add(new FilterPredicate(entry.getKey(), FilterOperator.EQUAL, filterParam));
      }
    }

    if (filterCollection.size() >= 2) {
      /* Composite Filter only works with 2 or more filters. */
      CompositeFilter combinedQueryFilter = new CompositeFilter(CompositeFilterOperator.AND, filterCollection);
      query.setFilter(combinedQueryFilter);
    } else if (filterCollection.size() == 1) {
      /* If a filter exists but it can't be composite, normal one is applied */
      query.setFilter(filterCollection.get(0));
    }
    return query;
  }

  public static boolean coerceParameterToBoolean(HttpServletRequest request, String key) {
    String requestParameter = request.getParameter(key);
    return (requestParameter != null) && (requestParameter.equals("true"));
  }
}