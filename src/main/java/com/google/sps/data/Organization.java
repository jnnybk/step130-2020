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
import com.google.appengine.api.datastore.Entity;

public final class Organization {

  private long id;
  private String name;
  private String email;
  private String address;
  private String city;
  private String state;
  private String zipcode;
  private String description;
  private String phoneNum;
  private long creationTimeStampMillis;
  private long lastEditedTimeStampMillis;
  private boolean isApproved;
  private String urlLink;
  private ArrayList<String> moderators;

  /* An Organization Object takes in an entity and assigns all of its fields based on the entity's
   * properties */

  public Organization(Entity entity) {
    this.id = (long) entity.getKey().getId();
    this.name = (String) entity.getProperty("orgName");
    this.email = (String) entity.getProperty("orgEmail");
    this.address = (String) entity.getProperty("orgStreetAddress");
    this.city = (String) entity.getProperty("orgCity");
    this.state = (String) entity.getProperty("orgState");
    this.zipcode = (String) entity.getProperty("orgZipCode");
    this.description = (String) entity.getProperty("orgDescription");
    this.phoneNum = (String) entity.getProperty("orgPhoneNum");
    this.creationTimeStampMillis = (long) entity.getProperty("creationTimeStampMillis");
    this.lastEditedTimeStampMillis = (long) entity.getProperty("lastEditTimeStampMillis");
    this.isApproved = (boolean) entity.getProperty("isApproved");
    this.urlLink = (String) entity.getProperty("orgUrl");
    this.moderators = (ArrayList) entity.getProperty("moderatorList");
  }
}
