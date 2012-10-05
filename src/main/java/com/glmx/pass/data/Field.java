/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass.data;

/**
 * "label" : "GATE", "key" : "gate", "value" : "23", "changeMessage" : "Gate changed to %@."
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
public class Field {

  private String label;
  private String key;
  private String value;
  private String changeMessage;

  public Field() {
  }
  
  public Field(String label, String key, String value) {
    this.label = label;
    this.key = key;
    this.value = value;
  }

  public Field(String label, String key, String value, String changeMessage) {
    this.label = label;
    this.key = key;
    this.value = value;
    this.changeMessage = changeMessage;
  }

  
  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * @param label the label to set
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * @return the key
   */
  public String getKey() {
    return key;
  }

  /**
   * @param key the key to set
   */
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value the value to set
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @return the changeMessage
   */
  public String getChangeMessage() {
    return changeMessage;
  }

  /**
   * @param changeMessage the changeMessage to set
   */
  public void setChangeMessage(String changeMessage) {
    this.changeMessage = changeMessage;
  }
}
