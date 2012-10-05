/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass.data;

import com.glmx.pass.dbo.PassDo;
import com.glmx.pass.dbo.enums.PassTypeEnum;
import java.io.IOException;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
public class BoardingPass extends Pass {

  public BoardingPass() {
  }

  public BoardingPass(PassDo dbo) throws IOException {
    super(dbo);
  }

  /**
   * @return the boardingPass
   */
  public DictionaryKeys getBoardingPass() {
    return fields;
  }

  /**
   * @param boardingPass the boardingPass to set
   */
  public void setBoardingPass(DictionaryKeys boardingPass) {
    this.fields = boardingPass;
  }

  @JsonIgnore
  @Override
  public String getPassType() {
    return PassTypeEnum.boardingPass.name();
  }
}
