package com.telstra.codechallenge;

import com.intuit.karate.junit5.Karate;

public class FunctionalIT {
  @Karate.Test
  Karate testMicroservice() {
    return Karate.run("microservice").relativeTo(getClass());
  }

  @Karate.Test
  Karate testUser() {
    return Karate.run("user").relativeTo(getClass());
  }

  @Karate.Test
  Karate testRepository() {
    return Karate.run("repository").relativeTo(getClass());
  }
}