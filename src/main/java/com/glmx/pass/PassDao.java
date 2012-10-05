/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass;

import com.glmx.pass.dbo.DeviceDo;
import com.glmx.pass.dbo.PassDo;
import com.glmx.pass.dbo.RegistrationDo;
import com.glmx.pass.dbo.enums.StatusEnum;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
@Repository
public class PassDao {

  @PersistenceContext
  private EntityManager entityManager;

  public Long addPass(PassDo data) {
    entityManager.persist(data);
    entityManager.merge(data);
    return data.getId();
  }

  public Long addDevice(DeviceDo data) {
    entityManager.persist(data);
    return data.getId();
  }

  public Long addRegistration(RegistrationDo data) {
    entityManager.persist(data);
    return data.getId();
  }

  public PassDo getPassBySerialNumber(String serialNumber, String typeId) {
    String hql = "from PassDo where serialNumber =? and passTypeIdentifier = ?";
    TypedQuery<PassDo> query = entityManager.createQuery(hql, PassDo.class);
    query.setParameter(1, serialNumber);
    query.setParameter(2, typeId);
    return getSingleResult(query);
  }

  public List<String> getSerialNumbers(String deviceId, String typeId, Date passesUpdatedSince) {
    String hql = "select p.serialNumber from PassDo p, DeviceDo d, RegistrationDo r "
            + "where p.passTypeIdentifier =? and p.id = r.passId and d.id = r.deviceId and d.uuid =?";
    if (passesUpdatedSince != null) {
      hql += " and p.modifiedDate >= ?";
    }
    TypedQuery<String> query = entityManager.createQuery(hql, String.class);
    query.setParameter(1, typeId);
    query.setParameter(2, deviceId);
    if (passesUpdatedSince != null) {
      query.setParameter(3, passesUpdatedSince);
    }
    return query.getResultList();
  }

  private <T> T getSingleResult(TypedQuery<T> query) {
    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public DeviceDo getDevice(String uuid) {
    String hql = "from DeviceDo where uuid = ?";
    TypedQuery<DeviceDo> query = entityManager.createQuery(hql, DeviceDo.class);
    query.setParameter(1, uuid);
    return getSingleResult(query);
  }

  public RegistrationDo getRegistration(String deviceId, String passTypeId, String serialNumber) {
    String hql = "select r from PassDo p, DeviceDo d, RegistrationDo r "
            + "where p.passTypeIdentifier =? and p.serialNumber = ? and p.id = r.passId and d.id = r.deviceId and d.uuid =?";
    TypedQuery<RegistrationDo> query = entityManager.createQuery(hql, RegistrationDo.class);
    query.setParameter(1, passTypeId);
    query.setParameter(2, serialNumber);
    query.setParameter(3, deviceId);
    return getSingleResult(query);
  }

  public List<DeviceDo> getDevices(String passTypeId, String serialNumber) {
    String hql = "select d from PassDo p, DeviceDo d, RegistrationDo r "
            + "where p.passTypeIdentifier =? and p.serialNumber = ? and p.id = r.passId and d.id = r.deviceId and r.status=?";
    TypedQuery<DeviceDo> query = entityManager.createQuery(hql, DeviceDo.class);
    query.setParameter(1, passTypeId);
    query.setParameter(2, serialNumber);
    query.setParameter(3, StatusEnum.Registered.name());
    return query.getResultList();
  }
  
  public void updateRegistration(RegistrationDo registration) {
    entityManager.persist(registration);
  }

  public void updatePass(PassDo dbo) {
    entityManager.persist(dbo);
  }

  public List<PassDo> getPasses(int index, int size) {
    TypedQuery<PassDo> query = entityManager.createQuery("from PassDo p left join fetch p.registrations "
            + "where p.deleted = 0 order by p.id desc", PassDo.class);
    query.setFirstResult(index);
    query.setMaxResults(size);
    return query.getResultList();    
  }

  public PassDo getPass(Long id) {
    return entityManager.find(PassDo.class, id);
  }
}
