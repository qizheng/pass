/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass.dbo;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
@Entity
@Table(name = "device")
public class DeviceDo {

  @Id
  @GeneratedValue
  private Long id;
  @Column(name = "uuid")
  private String uuid;
  @Column(name = "push_token")
  private String pushToken;
  @Column(name = "modified_Date")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Generated(GenerationTime.ALWAYS)
  private Date modifiedDate;

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the uuid
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * @param uuid the uuid to set
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * @return the pushToken
   */
  public String getPushToken() {
    return pushToken;
  }

  /**
   * @param pushToken the pushToken to set
   */
  public void setPushToken(String pushToken) {
    this.pushToken = pushToken;
  }

  /**
   * @return the modifiedDate
   */
  public Date getModifiedDate() {
    return modifiedDate;
  }

  /**
   * @param modifiedDate the modifiedDate to set
   */
  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }
}
