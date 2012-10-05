/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass.dbo;

import com.glmx.pass.dbo.enums.StatusEnum;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
@Entity
@Table(name = "registration")
public class RegistrationDo {

  @Id
  @GeneratedValue
  private Long id;
  @Column(name = "device_id")
  private Long deviceId;
  @Column(name = "pass_id")
  private Long passId;
  private String status = StatusEnum.Registered.name();
  @Column(name = "created_Date")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createdDate;
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
   * @return the deviceId
   */
  public Long getDeviceId() {
    return deviceId;
  }

  /**
   * @param deviceId the deviceId to set
   */
  public void setDeviceId(Long deviceId) {
    this.deviceId = deviceId;
  }

  /**
   * @return the passId
   */
  public Long getPassId() {
    return passId;
  }

  /**
   * @param passId the passId to set
   */
  public void setPassId(Long passId) {
    this.passId = passId;
  }

  /**
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @return the createdDate
   */
  public Date getCreatedDate() {
    return createdDate;
  }

  /**
   * @param createdDate the createdDate to set
   */
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
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
