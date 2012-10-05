/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;

public final class PKCS7Signer {

  private static final String KEY_ALIAS_IN_KEYSTORE = "Kelvin Zheng";
  private static final String SIGNATUREALGO = "SHA1withRSA";

  private String certUrl;
  private String certPass;
  private String wwdrIntermediateCertificatePath;

  public PKCS7Signer(String certUrl, String certPass, String wwdrIntermediateCertificatePath) {
    this.certUrl = certUrl;
    this.certPass = certPass;
    this.wwdrIntermediateCertificatePath = wwdrIntermediateCertificatePath;
  }
  
  public PKCS7Signer() {
  }

  public KeyStore loadKeyStore() throws Exception {

    KeyStore keystore = KeyStore.getInstance("PKCS12");
    InputStream is = new FileInputStream(certUrl);
    keystore.load(is, certPass.toCharArray());
    return keystore;
  }  
  
  public X509Certificate loadIntermediateCertificate() throws CertificateException, IOException {
    InputStream inStream = new FileInputStream(wwdrIntermediateCertificatePath);
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
    inStream.close();  
    return cert;
  }
  
  public CMSSignedDataGenerator setUpProvider(final KeyStore keystore, X509Certificate intermediateCert) throws Exception {

    Security.addProvider(new BouncyCastleProvider());

    Certificate[] certchain = (Certificate[]) keystore.getCertificateChain(KEY_ALIAS_IN_KEYSTORE);

    final List<Certificate> certlist = new ArrayList<Certificate>();

    for (int i = 0, length = certchain == null ? 0 : certchain.length; i < length; i++) {
      certlist.add(certchain[i]);
    }
    certlist.add(intermediateCert);

    Store certstore = new JcaCertStore(certlist);

    Certificate cert = keystore.getCertificate(KEY_ALIAS_IN_KEYSTORE);

    ContentSigner signer = new JcaContentSignerBuilder(SIGNATUREALGO).setProvider("BC").
            build((PrivateKey) (keystore.getKey(KEY_ALIAS_IN_KEYSTORE, certPass.toCharArray())));

    CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

    generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().
            setProvider("BC").
            build()).build(signer, (X509Certificate) cert));

    generator.addCertificates(certstore);

    return generator;
  }

  byte[] signPkcs7(final byte[] content, final CMSSignedDataGenerator generator) throws Exception {

    CMSTypedData cmsdata = new CMSProcessableByteArray(content);
    CMSSignedData signeddata = generator.generate(cmsdata, false);
    return signeddata.getEncoded();
  }

  public static void main(String[] args) throws Exception {

    PKCS7Signer signer = new PKCS7Signer("/Users/qizheng/Downloads/Certificates.p12", "glmx", "/Users/qizheng/Downloads/AppleWWDRCA.cer");
    KeyStore keyStore = signer.loadKeyStore();
    X509Certificate iCert = signer.loadIntermediateCertificate();
    CMSSignedDataGenerator signatureGenerator = signer.setUpProvider(keyStore, iCert);
    String content = "some bytes to be signed";
    byte[] signedBytes = signer.signPkcs7(content.getBytes("UTF-8"), signatureGenerator);
    System.out.println("Signed Encoded Bytes: " + new String(Base64.encode(signedBytes)));
  }
}
