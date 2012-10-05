/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass.dbo;

import com.glmx.pass.data.DictionaryKeys;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
@Entity
@Table(name = "pass")
public class PassDo implements Serializable {

  @Id
  @GeneratedValue
  private Long id;
  @Column(name = "serial_number")
  private String serialNumber;
  @Column(name = "type_identifier")
  private String passTypeIdentifier;
  @Column(name = "organization_name")
  private String organizationName;
  private String type;
  private String description;
  @Column(name = "logo_text")
  private String logoText;
  @Column(name = "foreground_color")
  private String foregroundColor;
  @Column(name = "background_Color")
  private String backgroundColor;
  @Column(name = "barCode_Type")
  private String barCodeType;
  @Column(name = "barCode_Text")
  private String barCodeText;
  private String locations;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "relevant_Date")
  private Date relevantDate;
  @Column(name = "header_Fields")
  private String headerFields;
  @Column(name = "primary_Fields")
  private String primaryFields;
  @Column(name = "secondary_Fields")
  private String secondaryFields;
  @Column(name = "auxiliary_Fields")
  private String auxiliaryFields;
  @Column(name = "back_Fields")
  private String backFields;
  @Column(name = "transit_Type")
  private String transitType;
  @Column(name = "created_Date")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createdDate;
  @Column(name = "modified_Date")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Generated(GenerationTime.ALWAYS)
  private Date modifiedDate;
  private Boolean deleted=false;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name="pass_id", insertable=false, updatable=false)
  @OrderBy("id")
  @BatchSize(size = 30)
  private List<RegistrationDo> registrations;
  
  public DictionaryKeys getFields() throws IOException {
    return new DictionaryKeys(this);
  }
  
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
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
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
   * @return the barCodeType
   */
  public String getBarCodeType() {
    return barCodeType;
  }

  /**
   * @param barCodeType the barCodeType to set
   */
  public void setBarCodeType(String barCodeType) {
    this.barCodeType = barCodeType;
  }

  /**
   * @return the barCodeText
   */
  public String getBarCodeText() {
    return barCodeText;
  }

  /**
   * @param barCodeText the barCodeText to set
   */
  public void setBarCodeText(String barCodeText) {
    this.barCodeText = barCodeText;
  }

  /**
   * @return the locations
   */
  public String getLocations() {
    return locations;
  }

  /**
   * @param locations the locations to set
   */
  public void setLocations(String locations) {
    this.locations = locations;
  }

  /**
   * @return the headerFields
   */
  public String getHeaderFields() {
    return headerFields;
  }

  /**
   * @param headerFields the headerFields to set
   */
  public void setHeaderFields(String headerFields) {
    this.headerFields = headerFields;
  }

  /**
   * @return the primaryFields
   */
  public String getPrimaryFields() {
    return primaryFields;
  }

  /**
   * @param primaryFields the primaryFields to set
   */
  public void setPrimaryFields(String primaryFields) {
    this.primaryFields = primaryFields;
  }

  /**
   * @return the secondaryFields
   */
  public String getSecondaryFields() {
    return secondaryFields;
  }

  /**
   * @param secondaryFields the secondaryFields to set
   */
  public void setSecondaryFields(String secondaryFields) {
    this.secondaryFields = secondaryFields;
  }

  /**
   * @return the auxiliaryFields
   */
  public String getAuxiliaryFields() {
    return auxiliaryFields;
  }

  /**
   * @param auxiliaryFields the auxiliaryFields to set
   */
  public void setAuxiliaryFields(String auxiliaryFields) {
    this.auxiliaryFields = auxiliaryFields;
  }

  /**
   * @return the backFields
   */
  public String getBackFields() {
    return backFields;
  }

  /**
   * @param backFields the backFields to set
   */
  public void setBackFields(String backFields) {
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

  /**
   * @return the relevantDate
   */
  public Date getRelevantDate() {
    return relevantDate;
  }

  /**
   * @param relevantDate the relevantDate to set
   */
  public void setRelevantDate(Date relevantDate) {
    this.relevantDate = relevantDate;
  }

  /**
   * @return the deleted
   */
  public Boolean getDeleted() {
    return deleted;
  }

  /**
   * @param deleted the deleted to set
   */
  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  /**
   * @return the registrations
   */
  public List<RegistrationDo> getRegistrations() {
    return registrations;
  }

  /**
   * @param registrations the registrations to set
   */
  public void setRegistrations(List<RegistrationDo> registrations) {
    this.registrations = registrations;
  }
}
