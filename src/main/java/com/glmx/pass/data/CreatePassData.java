/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass.data;

import com.glmx.pass.dbo.PassDo;
import com.glmx.pass.dbo.enums.PassTypeEnum;
import java.io.IOException;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
public class CreatePassData extends Pass {

  private PassTypeEnum typeEnum;

  public CreatePassData(PassDo dbo) throws IOException {
    super(dbo);
    typeEnum = PassTypeEnum.valueOf(dbo.getType());
  }

  public CreatePassData() {
  }
  
  public void setFields(DictionaryKeys fields) {
    this.fields = fields;
  }
  
  public DictionaryKeys getFields() {
    return this.fields;
  }
  
  @Override
  public String getPassType() {
    return typeEnum.name();
  }

  /**
   * @return the type
   */
  public PassTypeEnum getTypeEnum() {
    return typeEnum;
  }

  /**
   * @param type the type to set
   */
  public void setTypeEnum(PassTypeEnum type) {
    this.typeEnum = type;
  }
}
