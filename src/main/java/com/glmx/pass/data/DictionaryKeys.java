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

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
public class DictionaryKeys {

  private List<Field> headerFields;
  private List<Field> primaryFields;
  private List<Field> secondaryFields;
  private List<Field> auxiliaryFields;
  private List<Field> backFields;
  private String transitType;
  private static TypeReference listFields = new TypeReference<List<Field>>() { };

  public DictionaryKeys(PassDo dbo) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    this.headerFields = mapper.readValue(dbo.getHeaderFields(), listFields);
    this.primaryFields = mapper.readValue(dbo.getPrimaryFields(), listFields);
    this.auxiliaryFields = mapper.readValue(dbo.getAuxiliaryFields(), listFields);
    this.backFields = mapper.readValue(dbo.getBackFields(), listFields);
    this.secondaryFields = mapper.readValue(dbo.getSecondaryFields(), listFields);
    this.transitType = dbo.getTransitType();
  }
  
  public DictionaryKeys() {
  }

  /**
   * @return the headerFields
   */
  public List<Field> getHeaderFields() {
    return headerFields;
  }

  /**
   * @param headerFields the headerFields to set
   */
  public void setHeaderFields(List<Field> headerFields) {
    this.headerFields = headerFields;
  }

  /**
   * @return the primaryFields
   */
  public List<Field> getPrimaryFields() {
    return primaryFields;
  }

  /**
   * @param primaryFields the primaryFields to set
   */
  public void setPrimaryFields(List<Field> primaryFields) {
    this.primaryFields = primaryFields;
  }

  /**
   * @return the secondaryFields
   */
  public List<Field> getSecondaryFields() {
    return secondaryFields;
  }

  /**
   * @param secondaryFields the secondaryFields to set
   */
  public void setSecondaryFields(List<Field> secondaryFields) {
    this.secondaryFields = secondaryFields;
  }

  /**
   * @return the auxiliaryFields
   */
  public List<Field> getAuxiliaryFields() {
    return auxiliaryFields;
  }

  /**
   * @param auxiliaryFields the auxiliaryFields to set
   */
  public void setAuxiliaryFields(List<Field> auxiliaryFields) {
    this.auxiliaryFields = auxiliaryFields;
  }

  /**
   * @return the backFields
   */
  public List<Field> getBackFields() {
    return backFields;
  }

  /**
   * @param backFields the backFields to set
   */
  public void setBackFields(List<Field> backFields) {
    this.backFields = backFields;
  }

  /**
   * @return the transitType
   */
  public String getTransitType() {
    return transitType;
  }

  /**
   * @param transitType the transitType to set
   */
  public void setTransitType(String transitType) {
    this.transitType = transitType;
  }
}
