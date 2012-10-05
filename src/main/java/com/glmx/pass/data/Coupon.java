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
public class Coupon extends Pass {

  public Coupon() {
    super();
  }

  public Coupon(PassDo pass) throws IOException {
    super(pass);
  }
  
  /**
   * @return the coupon
   */
  public DictionaryKeys getCoupon() {
    return this.fields;
  }

  /**
   * @param coupon the coupon to set
   */
  public void setCoupon(DictionaryKeys coupon) {
    this.fields = coupon;
  }

  @JsonIgnore
  @Override
  public String getPassType() {
    return PassTypeEnum.coupon.name();
  }
}
