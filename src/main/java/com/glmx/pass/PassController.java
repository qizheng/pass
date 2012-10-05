/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass;

import com.glmx.pass.data.*;
import com.glmx.pass.dbo.DeviceDo;
import com.glmx.pass.dbo.PassDo;
import com.glmx.pass.dbo.RegistrationDo;
import com.glmx.pass.dbo.enums.PassTypeEnum;
import com.glmx.pass.dbo.enums.StatusEnum;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
@Controller
@Transactional
public class PassController {

  Logger log = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private PassDao passDao;
  @Value("${cert.url}")
  private String certUrl = "/Users/qizheng/Downloads/Certificates.p12";
  @Value("${cert.url.intermediate}")
  private String iCertUrl = "/Users/qizheng/Downloads/AppleWWDRCA.cer";
  @Value("${cert.password}")
  private String certPassword = "glmx";
  @Value("${app.host}")
  private String appHost;

  @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
  public void getPass(@PathVariable Long id, HttpServletResponse response) throws Exception {
    log.info("downloading pass " + id);
    PassDo dbo = passDao.getPass(id);
    if (dbo != null) {
      response.setContentType("application/vnd.apple.pkpass");
      response.setHeader("Content-Disposition", "inline; filename=\"" + dbo.getSerialNumber() + ".pkpass\"");
      File pass = signPass(dbo);
      log.info("psss is generated");
      response.setStatus(HttpStatus.OK.value());
      response.setHeader("Last-Modified", dbo.getModifiedDate().toGMTString());
      OutputStream out = response.getOutputStream();
      out.write(FileUtils.readFileToByteArray(pass));
      out.flush();
      out.close();
    } else {
      response.setStatus(HttpStatus.NOT_FOUND.value());
    }
  }

  @RequestMapping(value = "/issue/{id}", method = RequestMethod.GET)
  public void issuePass(@PathVariable Long id, HttpServletResponse response) throws Exception {
    log.info("issuing a new pass " + id);
    PassDo dbo = passDao.getPass(id);
    if (dbo != null) {
      PassDo pass = new PassDo();
      BeanUtils.copyProperties(dbo, pass, new String[]{"registrations"});
      pass.setId(null);
      pass.setSerialNumber(Ids.nextBase36Id());
      passDao.addPass(pass);
      getPass(pass.getId(), response);
      log.info("pass " + pass.getId() + " is issued");
    } else {
      response.setStatus(HttpStatus.NOT_FOUND.value());
    }
  }

  @RequestMapping(value = "/delete/{id}")
  public String deletePass(@PathVariable Long id) {
    log.info("deleting a pass " + id);
    PassDo dbo = passDao.getPass(id);
    if (dbo != null) {
      dbo.setDeleted(true);
      passDao.updatePass(dbo);
    }
    return "redirect:/";
  }

  @RequestMapping(value = "/v1/devices/{deviceId}/registrations/{passTypeId}/{serialNumber}", method = RequestMethod.POST)
  public ResponseEntity<String> registerPass(@PathVariable String deviceId, @PathVariable String passTypeId, @PathVariable String serialNumber,
          @RequestBody String payload) throws IOException {
    log.info("deviceId=" + deviceId);
    log.info("passTypeId=" + passTypeId);
    log.info("serialNumber=" + serialNumber);
    log.info("payload=" + URLDecoder.decode(payload, "UTF-8"));
    ObjectMapper mapper = new ObjectMapper();
    RegisterPass data = mapper.readValue(URLDecoder.decode(payload, "UTF-8"), RegisterPass.class);
    log.info("pushToken=" + data.getPushToken());
    DeviceDo device = passDao.getDevice(deviceId);
    if (device == null) {
      device = new DeviceDo();
      device.setUuid(deviceId);
      device.setPushToken(data.getPushToken());
      device.setId(passDao.addDevice(device));
    }
    PassDo pass = passDao.getPassBySerialNumber(serialNumber, passTypeId);
    if (pass != null) {
      // check if the pass has been registered
      RegistrationDo registration = passDao.getRegistration(deviceId, passTypeId, serialNumber);
      if (registration != null) {
        return new ResponseEntity<String>("", HttpStatus.OK); //200
      }
      registration = new RegistrationDo();
      registration.setDeviceId(device.getId());
      registration.setCreatedDate(new Date());
      registration.setPassId(pass.getId());
      passDao.addRegistration(registration);
      return new ResponseEntity<String>("", HttpStatus.CREATED); //201
    } else {
      return new ResponseEntity<String>("", HttpStatus.NOT_FOUND); //no such a pass
    }
  }

  @RequestMapping(value = "/v1/devices/{deviceId}/registrations/{passTypeId}/{serialNumber}", method = RequestMethod.DELETE)
  public ResponseEntity<String> unRegisterPass(@PathVariable String deviceId, @PathVariable String passTypeId, @PathVariable String serialNumber) {
    log.info("deviceId=" + deviceId);
    log.info("passTypeId=" + passTypeId);
    log.info("serialNumber=" + serialNumber);
    RegistrationDo registration = passDao.getRegistration(deviceId, passTypeId, serialNumber);
    if (registration != null) {
      registration.setStatus(StatusEnum.Unregistered.name());
      passDao.updateRegistration(registration);
    }
    return new ResponseEntity<String>("", HttpStatus.OK); //200
  }

  @RequestMapping(value = "/v1/devices/{deviceId}/registrations/{passTypeId}", method = RequestMethod.GET)
  public ResponseEntity<String> getSerialNumbers(@PathVariable String deviceId, @PathVariable String passTypeId,
          @RequestParam(required = false) String passesUpdatedSince, HttpServletRequest request) throws IOException {
    String url = request.getRequestURI();
    passTypeId = url.substring(url.lastIndexOf("/") + 1);
    log.info("deviceId=" + deviceId);
    log.info("passTypeId=" + passTypeId);
    List<String> serialNumbers = passDao.getSerialNumbers(deviceId, passTypeId,
            passesUpdatedSince == null ? null : new Date(passesUpdatedSince));
    SerialNumbers resp = new SerialNumbers(new Date().toString(), serialNumbers.toArray(new String[0]));
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.defaultPrettyPrintingWriter().writeValueAsString(resp);
    return new ResponseEntity<String>(json, HttpStatus.OK); //200
  }

  @RequestMapping(value = "/v1/passes/{passTypeId}/{serialNumber}", method = RequestMethod.GET)
  public void latestPass(@PathVariable String passTypeId, @PathVariable String serialNumber,
          @RequestHeader(value = "if-modified-since", required = false) String modifiedSince,
          HttpServletResponse response) throws Exception {
    log.info("passTypeId=" + passTypeId);
    log.info("serialNumber=" + serialNumber);
    log.info("modifiedSince=" + modifiedSince);
    Date requestDate = null;
    if (modifiedSince != null) {
      requestDate = new Date(modifiedSince);
    }

    PassDo dbo = passDao.getPassBySerialNumber(serialNumber, passTypeId);
    if (dbo == null) {
      response.setStatus(HttpStatus.NOT_FOUND.value());
    } else if (requestDate == null || requestDate.before(dbo.getModifiedDate())) {
      File pass = signPass(dbo);
      response.setStatus(HttpStatus.OK.value());
      response.setHeader("Last-Modified", dbo.getModifiedDate().toGMTString());
      OutputStream out = response.getOutputStream();
      out.write(FileUtils.readFileToByteArray(pass));
      out.flush();
      out.close();
    } else {
      response.setStatus(HttpStatus.NOT_MODIFIED.value());
    }
  }

  @RequestMapping(value = "/v1/log", method = RequestMethod.POST)
  public ResponseEntity<String> log(@RequestBody String payload) throws UnsupportedEncodingException {
    log.info(URLDecoder.decode(payload, "UTF-8"));
    return new ResponseEntity<String>("", HttpStatus.OK); //200
  }

  @RequestMapping(value = "/")
  public String home(Model model) {
    List<PassDo> passes = passDao.getPasses(0, 100);
    model.addAttribute("passes", passes);
    model.addAttribute("appHost", appHost);
    return "home";
  }

  @RequestMapping(value = "/add", method = RequestMethod.GET)
  public String addPass(Model model) {
    model.addAttribute("pass", new CreatePassData());
    return "new";
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public String addPass(@ModelAttribute("pass") CreatePassData pass,
          HttpServletRequest request, Model model) throws IOException {
    preprocessData(pass);
    PassDo dbo = pass.toDbo();
    passDao.addPass(dbo);
    return "redirect:/";
  }

  @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
  public String editPass(@PathVariable Long id, Model model) throws IOException {
    PassDo dbo = passDao.getPass(id);
    if (dbo != null) {
      CreatePassData data = new CreatePassData(dbo);
      model.addAttribute("pass", data);
      model.addAttribute("edit", true);
    }
    return "new";
  }

  @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
  public String editPass(@PathVariable Long id, @ModelAttribute("pass") CreatePassData pass,
          HttpServletRequest request, Model model) throws IOException {
    preprocessData(pass);
    PassDo dbo = pass.toDbo();
    PassDo exist = passDao.getPass(id);
    dbo.setSerialNumber(exist.getSerialNumber());
    BeanUtils.copyProperties(dbo, exist, new String[]{"registrations"});
    exist.setId(id);
    passDao.updatePass(exist);

    // notify user
    if (exist.getSerialNumber() != null) {
      List<DeviceDo> devices = passDao.getDevices(exist.getPassTypeIdentifier(), exist.
              getSerialNumber());
      for (DeviceDo device : devices) {
//        String simplePayload = APNS.newPayload().build();
        ApnsService service = APNS.newService().withCert(certUrl, certPassword).
                withProductionDestination().build();
        service.push(device.getPushToken(), "{}");
      }
    }
    return "redirect:/";
  }
/*
  private DictionaryKeys getCouponFields() {
    DictionaryKeys coupon = new DictionaryKeys();
    Field[] primaryFields = new Field[]{new Field("Lunch", "offer", "20% off", "%@")};
    Field[] auxiliaryFields = new Field[]{new Field("EXPIRES", "expires", "2 weeks")};
    Field[] backFields = new Field[]{new Field("TERMS AND CONDITIONS", "terms", "Generico offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"as is,\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licensors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. "
      + "Generico reserves the right to change or discontinue at any time any aspect or feature of the pass.")};
    coupon.setPrimaryFields(Arrays.asList(primaryFields));
    coupon.setAuxiliaryFields(Arrays.asList(auxiliaryFields));
    coupon.setBackFields(Arrays.asList(backFields));
    return coupon;
  }

  private DictionaryKeys getBoardingPassFields() {
    DictionaryKeys boardingPass = new DictionaryKeys();
    Field[] headerFields = new Field[]{new Field("GATE", "gate", "23", "Gate changed to %@.")};
    Field[] primaryFields = new Field[]{new Field("SAN FRANCISCO", "depart", "SFO"),
      new Field("NEW YORK", "arrive", "JFK")};
    Field[] secondaryFields = new Field[]{new Field("PASSENGER", "passenger", "John Appleseed")};
    Field[] auxiliaryFields = new Field[]{
      new Field("DEPART", "boardingTime", "3:25 PM", "Boarding time changed to %@."),
      new Field("FLIGHT", "flightNewName", "815", "Flight number changed to %@"),
      new Field("DESIG.", "class", "Coach"),
      new Field("DATE", "date", "8/27")
    };
    Field[] backFields = new Field[]{
      new Field("PASSPORT", "passport", "Canadian/Canadien"),
      new Field("RESIDENCE", "residence", "5780 E Mission St, San Jose, CA"),
      new Field("TERMS AND CONDITIONS", "terms", "Generico offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"as is,\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licensors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. "
      + "Generico reserves the right to change or discontinue at any time any aspect or feature of the pass.")
    };
    boardingPass.setHeaderFields(Arrays.asList(headerFields));
    boardingPass.setPrimaryFields(Arrays.asList(primaryFields));
    boardingPass.setAuxiliaryFields(Arrays.asList(auxiliaryFields));
    boardingPass.setBackFields(Arrays.asList(backFields));
    boardingPass.setSecondaryFields(Arrays.asList(secondaryFields));
    return boardingPass;
  }
  * 
  */


  private File signCouponPass(Pass pass) throws Exception {
    pass.setWebServiceURL(this.appHost);
    SignPass signPass = new SignPass("/src/data/Coupon.raw",
            certUrl, certPassword, iCertUrl,
            "/src/data/Coupon.pkpass", true);
    return signPass.signPass(pass, true);
  }

  private File signBoaringPass(BoardingPass pass) throws Exception {
    pass.setWebServiceURL(this.appHost);
    SignPass signPass = new SignPass("/src/data/BoardingPass.raw",
            certUrl, certPassword, iCertUrl,
            "/src/data/BoardingPass.pkpass", true);
    return signPass.signPass(pass, true);
  }

  private File signPass(PassDo pass) throws Exception {
    if (pass.getType().equals(PassTypeEnum.boardingPass.name())) {
      return signBoaringPass(new BoardingPass(pass));
    } else if (pass.getType().equals(PassTypeEnum.eventTicket.name())) {
      return signCouponPass(new EventTicket(pass));
    } else if (pass.getType().equals(PassTypeEnum.storeCard.name())) {
      return signCouponPass(new StoreCard(pass));
    } else if (pass.getType().equals(PassTypeEnum.coupon.name())) {
      return signCouponPass(new Coupon(pass));
    } else {
      return signCouponPass(new Generic(pass));
    }
  }

  private void preprocessData(CreatePassData pass) {
    if (pass.getRelevantDate() != null && !pass.getRelevantDate().isEmpty() && pass.getRelevantDate().
            length() < 11) {
      pass.setRelevantDate(pass.getRelevantDate() + "T10:06:30+01:00");
    }
    // remove empty fields
    truncatFields(pass.getFields().getHeaderFields());
    truncatFields(pass.getFields().getPrimaryFields());
    truncatFields(pass.getFields().getAuxiliaryFields());
    truncatFields(pass.getFields().getSecondaryFields());
    truncatFields(pass.getFields().getBackFields());
  }

  private void truncatFields(List<Field> fields) {
    if (fields != null) {
      for (int i = fields.size() - 1; i >= 0; i--) {
        Field f = fields.get(i);
        if (f.getValue() == null || f.getValue().isEmpty()) {
          fields.remove(i);
        }
      }
    }
  }
}
