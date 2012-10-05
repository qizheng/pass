/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass.data;

import com.glmx.pass.dbo.PassDo;
import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
public abstract class Pass {

  private int formatVersion = 1;
  private String passTypeIdentifier = "pass.tello.coupon"; 
  private String serialNumber;
  private String teamIdentifier = "6MSJK492D9";
  private String webServiceURL;
  private String authenticationToken = "vxwxd7J8AlNNFPS8k0a0FfUFtq0ewzFdc";
  protected BarCode barcode;
  protected List<Location> locations;
  private String relevantDate;
  private String organizationName = "Chipotle";
//  private String organizationName = "Tello, Inc.";
  private String description;
  private String logoText;
  private String foregroundColor;
  private String backgroundColor;
  
  protected DictionaryKeys fields;
  private static TypeReference listLocations = new TypeReference<List<Location>>() { };
  public static DateTimeFormatter isoDataFormatter = ISODateTimeFormat.dateTimeNoMillis();


  public Pass() {
  }
  
  public Pass(PassDo dbo) throws IOException {
    BeanUtils.copyProperties(dbo, this, new String[]{"locations", "relevantDate"});
    this.fields = new DictionaryKeys(dbo);
    if (dbo.getBarCodeType() != null) {
      BarCode barCode = new BarCode();
      this.barcode = barCode;
      barCode.setFormat(dbo.getBarCodeType());
      barCode.setMessage(dbo.getBarCodeText());
    }
    if (dbo.getLocations() != null) {
      ObjectMapper mapper = new ObjectMapper();
      this.locations = mapper.readValue(dbo.getLocations(), listLocations);
    }
    this.relevantDate = dbo.getRelevantDate() == null ? 
            null : isoDataFormatter.print(dbo.getRelevantDate().getTime());
  }
  
  public PassDo toDbo() throws IOException {
    PassDo dbo = new PassDo();
    BeanUtils.copyProperties(this, dbo, new String[]{"locations", "relevantDate"});
    if (this.barcode != null) {
      dbo.setBarCodeText(barcode.getMessage());
      dbo.setBarCodeType(barcode.getFormat());
    }
    ObjectMapper mapper = new ObjectMapper();
    dbo.setLocations(mapper.writeValueAsString(this.locations));
    dbo.setBackFields(mapper.writeValueAsString(this.fields.getBackFields()));
    dbo.setAuxiliaryFields(mapper.writeValueAsString(this.fields.getAuxiliaryFields()));
    dbo.setHeaderFields(mapper.writeValueAsString(this.fields.getHeaderFields()));
    dbo.setPrimaryFields(mapper.writeValueAsString(this.fields.getPrimaryFields()));
    dbo.setSecondaryFields(mapper.writeValueAsString(this.fields.getSecondaryFields()));
    dbo.setTransitType(this.fields.getTransitType());
    dbo.setType(getPassType());
    dbo.setRelevantDate(relevantDate == null || relevantDate.isEmpty() ? null : 
            Pass.isoDataFormatter.parseLocalDateTime(relevantDate).toDate());
    return dbo;
  }
  
  abstract public String getPassType();
  
  /**
   * @return the formatVersion
   */
  public int getFormatVersion() {
    return formatVersion;
  }

  /**
   * @param formatVersion the formatVersion to set
   */
  public void setFormatVersion(int formatVersion) {
    this.formatVersion = formatVersion;
  }

  /**
   * @return the passTypeIdentifier
   */
  public String getPassTypeIdentifier() {
    return passTypeIdentifier;
  }

  /**
   * @param passTypeIdentifier the passTypeIdentifier to set
   */
  public void setPassTypeIdentifier(String passTypeIdentifier) {
    this.passTypeIdentifier = passTypeIdentifier;
  }

  /**
   * @return the serialNumber
   */
  public String getSerialNumber() {
    return serialNumber;
  }

  /**
   * @param serialNumber the serialNumber to set
   */
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  /**
   * @return the teamIdentifier
   */
  public String getTeamIdentifier() {
    return teamIdentifier;
  }

  /**
   * @param teamIdentifier the teamIdentifier to set
   */
  public void setTeamIdentifier(String teamIdentifier) {
    this.teamIdentifier = teamIdentifier;
  }

  /**
   * @return the webServiceURL
   */
  public String getWebServiceURL() {
    return webServiceURL;
  }

  /**
   * @param webServiceURL the webServiceURL to set
   */
  public void setWebServiceURL(String webServiceURL) {
    this.webServiceURL = webServiceURL;
  }

  /**
   * @return the authenticationToken
   */
  public String getAuthenticationToken() {
    return authenticationToken;
  }

  /**
   * @param authenticationToken the authenticationToken to set
   */
  public void setAuthenticationToken(String authenticationToken) {
    this.authenticationToken = authenticationToken;
  }

  /**
   * @return the barcode
   */
  public BarCode getBarcode() {
    return barcode;
  }

  /**
   * @param barcode the barcode to set
   */
  public void setBarcode(BarCode barcode) {
    this.barcode = barcode;
  }

  /**
   * @return the locations
   */
  public List<Location> getLocations() {
    return locations;
  }

  /**
   * @param locations the locations to set
   */
  public void setLocations(List<Location> locations) {
    this.locations = locations;
  }

  /**
   * @return the organizationName
   */
  public String getOrganizationName() {
    return organizationName;
  }

  /**
   * @param organizationName the organizationName to set
   */
  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the logoText
   */
  public String getLogoText() {
    return logoText;
  }

  /**
   * @param logoText the logoText to set
   */
  public void setLogoText(String logoText) {
    this.logoText = logoText;
  }

  /**
   * @return the foregroundColor
   */
  public String getForegroundColor() {
    return foregroundColor;
  }

  /**
   * @param foregroundColor the foregroundColor to set
   */
  public void setForegroundColor(String foregroundColor) {
    this.foregroundColor = foregroundColor;
  }

  /**
   * @return the backgroundColor
   */
  public String getBackgroundColor() {
    return backgroundColor;
  }

  /**
   * @param backgroundColor the backgroundColor to set
   */
  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  /**
   * @return the relevantDate
   */
  public String getRelevantDate() {
    return relevantDate;
  }

  /**
   * @param relevantDate the relevantDate to set
   */
  public void setRelevantDate(String relevantDate) {
    this.relevantDate = relevantDate;
  }
}
