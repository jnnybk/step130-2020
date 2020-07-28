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
import com.google.sps.servlets.ListEventsServlet;
import com.google.sps.data.ListHelper;
import java.io.IOException;

public class ListEventsHelper extends ListHelper {

  public static Query getQuery(String entityKind, HttpServletRequest request, GivrUser currentUser) {
    
    HashMap<String, String> datastoreConstantMap = new HashMap<String, String>(ListEventsServlet.constantMap);

    /* First the filter params in the request are parsed into a map that applies each filter to the query */
    HashMap<String, ArrayList<String>> filterParamMap = parseFilterParams(entityKind, request, currentUser, datastoreConstantMap);

    /* displayForUser is set to true when the user wants whatever entity they are querying
     * (Distributors, Events), to only return the entities they belong to / are involved with.*/
    boolean displayForUser = coerceParameterToBoolean(request, "displayForUser");

    ArrayList<Filter> filterCollection = new ArrayList<Filter>();
    filterCollection = handleUserFiltering(entityKind, currentUser, displayForUser);

    return getQueryFromFilters(entityKind, filterParamMap, filterCollection);
  }

  /* handleUserFiltering handles filtering related to a user's role and permissions, and whether that user has requested to only
   * see organizations or events they belong to */
  public static ArrayList<Filter> handleUserFiltering(String entityKind, GivrUser currentUser, boolean displayForUser) {
    ArrayList<Filter> filterCollection = new ArrayList<Filter>();
    if (displayForUser) {
      ArrayList<Entity> moderatingOrgs = currentUser.getModeratingOrgs();

      ArrayList<Long> moderatingOrgIds = new ArrayList<Long>();
      for (Entity entity : moderatingOrgs) {
        moderatingOrgIds.add(entity.getKey().getId());
      }
      filterCollection.add(new FilterPredicate("eventOwnerOrgId", FilterOperator.IN, moderatingOrgIds));
    }
    return filterCollection;
  }
}