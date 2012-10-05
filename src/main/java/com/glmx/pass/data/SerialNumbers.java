/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass.data;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
public class SerialNumbers {
  
  private String lastUpdated;
  private String[] serialNumbers;

  public SerialNumbers(String lastUpdated, String[] serialNumbers) {
    this.lastUpdated = lastUpdated;
    this.serialNumbers = serialNumbers;
  }

  /**
   * @return the lastUpdated
   */
  public String getLastUpdated() {
    return lastUpdated;
  }

  /**
   * @param lastUpdated the lastUpdated to set
   */
  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  /**
   * @return the serialNumbers
   */
  public String[] getSerialNumbers() {
    return serialNumbers;
  }

  /**
   * @param serialNumbers the serialNumbers to set
   */
  public void setSerialNumbers(String[] serialNumbers) {
    this.serialNumbers = serialNumbers;
  }
  
}
