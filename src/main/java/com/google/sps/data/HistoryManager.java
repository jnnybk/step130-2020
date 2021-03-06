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

import java.util.ArrayList;
import java.util.Arrays;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


public final class HistoryManager {

  public EmbeddedEntity recordHistory(String historyMessage, long millisecondSinceEpoch) {
    EmbeddedEntity historyEntry = new EmbeddedEntity();

    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();

    historyEntry.setProperty("changeAuthorId", userId);
    historyEntry.setProperty("changeMessage", historyMessage);
    historyEntry.setProperty("changeTimeStampMillis", millisecondSinceEpoch);

    return historyEntry;
  }
}
