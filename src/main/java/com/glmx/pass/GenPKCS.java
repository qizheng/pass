/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
public class GenPKCS {

  private String certUrl;
  private String certPass;

  public GenPKCS(String certUrl, String certPass) {
    this.certUrl = certUrl;
    this.certPass = certPass;
  }

  public byte[] sign(byte[] dataToSign) throws Exception {

    //First load the keystore object by providing the p12 file path
    KeyStore clientStore = KeyStore.getInstance("PKCS12");
    //replace testPass with the p12 password/pin
    clientStore.load(new FileInputStream(certUrl), certPass.toCharArray());

    Enumeration<String> aliases = clientStore.aliases();
    String aliaz = "";
    while (aliases.hasMoreElements()) {
      aliaz = aliases.nextElement();
      if (clientStore.isKeyEntry(aliaz)) {
        break;
      }
    }
    X509Certificate c = (X509Certificate) clientStore.getCertificate(aliaz);

    //compute signature:
    Signature signature = Signature.getInstance("Sha1WithRSA");
    signature.initSign((PrivateKey) clientStore.getKey(aliaz, certPass.toCharArray()));
    signature.update(dataToSign);
    byte[] signedData = signature.sign();

    //load X500Name
    X500Name xName = X500Name.asX500Name(c.getSubjectX500Principal());
    //load serial number
    BigInteger serial = c.getSerialNumber();
    //laod digest algorithm
    AlgorithmId digestAlgorithmId = new AlgorithmId(AlgorithmId.SHA_oid);
    //load signing algorithm
    AlgorithmId signAlgorithmId = new AlgorithmId(AlgorithmId.RSAEncryption_oid);

    //Create SignerInfo:
    SignerInfo sInfo = new SignerInfo(xName, serial, digestAlgorithmId, signAlgorithmId, signedData);
    //Create ContentInfo:
    ContentInfo cInfo = new ContentInfo(ContentInfo.DIGESTED_DATA_OID, new DerValue(DerValue.tag_OctetString, dataToSign));
    //Create PKCS7 Signed data
    PKCS7 p7 = new PKCS7(new AlgorithmId[]{digestAlgorithmId}, cInfo,
            new java.security.cert.X509Certificate[]{c},
            new SignerInfo[]{sInfo});
    //Write PKCS7 to byteArray
    ByteArrayOutputStream bOut = new DerOutputStream();
    p7.encodeSignedData(bOut);

    return bOut.toByteArray();
  }
}
