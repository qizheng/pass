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
public class StoreCard extends Pass {

  public StoreCard(PassDo dbo) throws IOException {
    super(dbo);
  }

  public StoreCard() {
  }
  
  /**
   * @return the coupon
   */
  public DictionaryKeys getStoreCard() {
    return this.fields;
  }

  /**
   * @param coupon the coupon to set
   */
  public void setStoreCard(DictionaryKeys coupon) {
    this.fields = coupon;
  }
  
  
  @JsonIgnore
  @Override
  public String getPassType() {
    return PassTypeEnum.storeCard.name();
  }
  
}
