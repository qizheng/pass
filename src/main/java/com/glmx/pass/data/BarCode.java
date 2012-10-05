/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass.data;

/**
 *
 *  "message" : "SFOJFK JOHN APPLESEED LH451 2012-07-22T14:25-08:00",
 *  "format" : "PKBarcodeFormatPDF417",
 *  "messageEncoding" : "iso-8859-1"
 * 
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
public class BarCode {
  
  private String message;
  private String format;
  private String messageEncoding = "iso-8859-1";

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @return the format
   */
  public String getFormat() {
    return format;
  }

  /**
   * @param format the format to set
   */
  public void setFormat(String format) {
    this.format = format;
  }

  /**
   * @return the messageEncoding
   */
  public String getMessageEncoding() {
    return messageEncoding;
  }

  /**
   * @param messageEncoding the messageEncoding to set
   */
  public void setMessageEncoding(String messageEncoding) {
    this.messageEncoding = messageEncoding;
  }
  
}
