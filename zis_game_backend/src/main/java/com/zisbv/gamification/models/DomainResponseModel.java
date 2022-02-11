package com.zisbv.gamification.models;

import java.util.List;

public class DomainResponseModel {


  private List<String> domains;

  public List<String> getDomains() {
    return domains;
  }

  public void setDomains(List<String> domains) {
    this.domains = domains;
  }

  @Override
  public String toString() {
    return "DomainResponseModel [domains=" + domains + "]";
  }


}
