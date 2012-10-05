/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass.data;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
public class Location {

  private double longitude;
  private double latitude;

  public Location() {
  }

  public Location(double longitude, double latitude) {
    this.longitude = longitude;
    this.latitude = latitude;
  }

  /**
   * @return the longitude
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * @param longitude the longitude to set
   */
  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  /**
   * @return the latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * @param latitude the latitude to set
   */
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }
}
