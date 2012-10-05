/*
 * Copyright (c) 2012. Global Liquid Markets, LLC.
 * All Rights Reserved.
 */

package com.glmx.pass;

import com.glmx.pass.data.*;
import java.io.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author Kelvin Zheng <kelvin@glmx.com>
 */
public class SignPass {
  
  private static final Log log = LogFactory.getLog(SignPass.class);
  private static int TEMP_DIR_ATTEMPTS = 1000;
  
  private String passUrl;
  private String certificateUrl;
  private String iCertificateUrl;
  private String certificatePassword;
  private String outputUrl;
  private boolean compreseIntoZipFile;

  public SignPass(String passUrl, String certificateUrl, String certificatePassword, String iCertificateUrl, String outputUrl, boolean compreseIntoZipFile) {
    this.passUrl = passUrl;
    this.certificateUrl = certificateUrl;
    this.certificatePassword = certificatePassword;
    this.outputUrl = outputUrl;
    this.compreseIntoZipFile = compreseIntoZipFile;
    this.iCertificateUrl = iCertificateUrl;
  }
  
  public File signPass(Pass pass, boolean forceClean) throws IOException, Exception {
    // Validate that requested contents are not a signed and expanded pass archive.
    validateDirectoryAsUnsignedRawPass(forceClean);

    // Get a temporary place to stash the pass contents
    File tempDir = this.createTempDirectoty();

    // Make a copy of the pass contents to the temporary folder
    this.copyPassToTemporaryLocation(tempDir);

    // Clean out the unneeded .DS_Store files
    this.cleanDsStoreFiles(tempDir);
    
    // Build pass file
    this.generatePass(pass, tempDir);

    // Build the json manifest
    File manifest = this.generateJsonManifest(tempDir);

    // Sign the manifest
    this.signManifest(manifest, tempDir.getAbsolutePath());

    // Package pass
    File out = this.compresspassFile(tempDir);

    // Clean up the temp directory
    FileUtils.deleteDirectory(tempDir);
    return out;
  }

  /*
   * Ensures that the raw pass directory does not contain signatures
   *
   */
  private void validateDirectoryAsUnsignedRawPass(boolean forceClean) {
    if (forceClean) {
      forceCleanRawPass();
    }
    
    boolean has_manifiest = new File(this.passUrl + File.pathSeparator + "manifest.json").exists();
    log.info("Raw pass has manifest? " + has_manifiest);
    
    boolean has_signiture = new File(this.passUrl + File.pathSeparator + "/signature").exists();
    log.info("Raw pass has signature? " + has_signiture);
    
    if (has_signiture || has_manifiest) {
      throw new RuntimeException("#{self.pass_url} contains pass signing artificats that need to be removed before signing.");
    }
  }
  
  private void forceCleanRawPass() {
    log.info("Force cleaning the raw pass directory.");
    new File(this.passUrl + File.pathSeparator + "manifest.json").deleteOnExit();
    new File(this.passUrl + File.pathSeparator + "signature").deleteOnExit();
  }

  /*
   * Creates a temporary place to work with the pass files without polluting the original
   *
   */
  private File createTempDirectoty() {
    log.info("Create temp directory.");
    File baseDir = new File(System.getProperty("java.io.tmpdir"));
    String baseName = System.currentTimeMillis() + "-";
    
    for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
      File tempDir = new File(baseDir, baseName + counter);
      if (tempDir.mkdir()) {
        return tempDir;
      }
    }
    throw new IllegalStateException("Failed to create directory within "
            + TEMP_DIR_ATTEMPTS + " attempts (tried "
            + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
  }

  /*
   * Copies the pass contents to the temporary location
   *
   */
  private void copyPassToTemporaryLocation(File temporaryDirectory) throws IOException {
    log.info("Copying pass to temp directory.");
    FileUtils.copyDirectory(new File(this.passUrl), temporaryDirectory);
  }

  /*
   * Removes .DS_Store files if they exist
   *
   */
  private void cleanDsStoreFiles(File temporaryDirectory) {
    log.info("Cleaning .DS_Store files");
    FileFilter fileFilter = new WildcardFileFilter("**/.DS_Store");
    File[] files = temporaryDirectory.listFiles(fileFilter);
    for (File f : files) {
      f.delete();
    }
  }

  /*
   * Creates a json manifest where each files contents has a SHA1 hash
   *
   */
  private File generatePass(Pass pass, File temporaryDirectory) throws IOException {
    log.info("Generating JSON pass");
    //Write the hash dictionary out to a pass file
    File f = new File(temporaryDirectory, "/pass.json");
    ObjectMapper mapper = new ObjectMapper();
    mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);     
    mapper.defaultPrettyPrintingWriter().writeValue(f, pass);
    return f;
  }
  
  /*
   * Creates a json manifest where each files contents has a SHA1 hash
   *
   */
  private File generateJsonManifest(File temporaryDirectory) throws IOException {
    log.info("Generating JSON manifest");
    Map<String, String> manifest = new HashMap();
    //Gather all the files and generate a sha1 hash
    File[] files = temporaryDirectory.listFiles();
    for (File f : files) {
      manifest.put(f.getName(), DigestUtils.shaHex(new FileInputStream(f)));
    }

    //Write the hash dictionary out to a manifest file
    File f = new File(temporaryDirectory, "/manifest.json");
    ObjectMapper mapper = new ObjectMapper();
    mapper.defaultPrettyPrintingWriter().writeValue(f, manifest);
    return f;
  }
  
  private void signManifest(File manifest, String temPath) throws Exception {
    log.info("Signing the manifest in "+temPath);
//    GenPKCS pkcs = new GenPKCS(this.certificateUrl, this.certificatePassword);
//    byte[] signed = pkcs.sign(FileUtils.readFileToByteArray(manifest));
    PKCS7Signer signer = new PKCS7Signer(this.certificateUrl, this.certificatePassword, this.iCertificateUrl);
    KeyStore keyStore = signer.loadKeyStore();
    X509Certificate iCert = signer.loadIntermediateCertificate();
    CMSSignedDataGenerator signatureGenerator = signer.setUpProvider(keyStore, iCert);
    byte[] signed = signer.signPkcs7(FileUtils.readFileToByteArray(manifest), signatureGenerator);

    // reate an output path for the signed data
    String signaturePath = temPath + "/signature";

    // Write out the data
    FileUtils.writeByteArrayToFile(new File(signaturePath), signed);
  }
  
  private File compresspassFile(File temporaryDirectory) throws FileNotFoundException, IOException {
    log.info("Compressing the pass");
    File zippedFile = new File(this.outputUrl);
    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zippedFile));
    File[] files = temporaryDirectory.listFiles();
    byte[] b = new byte[1024];
    for (File f : files) {
      out.putNextEntry(new ZipEntry(f.getName()));
      int count;
      FileInputStream in = new FileInputStream(f);
      while ((count = in.read(b)) > 0) {
        out.write(b, 0, count);
      }
      in.close();
    }
    out.close();
    return zippedFile;
  }
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws Exception {
    SignPass signPass = new SignPass("/src/data/BoardingPass.raw", 
            "/Users/qizheng/Downloads/Certificates.p12", 
            "glmx", "/Users/qizheng/Downloads/AppleWWDRCA.cer", "/src/data/BoardingPass.pkpass", true);
    Coupon pass = new Coupon();
    pass.setSerialNumber(UUID.randomUUID().toString());
    pass.setAuthenticationToken("vxwxd7J8AlNNFPS8k0a0FfUFtq0ewzFdc");
    pass.setWebServiceURL("https://example.com/passes/");
    BarCode barCode = new BarCode();
    barCode.setFormat("PKBarcodeFormatPDF417");
    barCode.setMessageEncoding("iso-8859-1");
    barCode.setMessage(pass.getSerialNumber());
    pass.setBarcode(barCode);
    List<Location> locations = new ArrayList(2);
    locations.add(new Location(-122.3748889, 37.6189722));
    locations.add(new Location(-122.03118, 37.33182));
    pass.setLocations(locations);
    pass.setDescription("Mandarin Gourmet Coupon");
    pass.setLogoText("Mandarin Gourmet");
    pass.setForegroundColor("rgb(255, 255, 255)");
    pass.setBackgroundColor("rgb(206, 140, 53)");
    DictionaryKeys coupon = new DictionaryKeys();
    Field[] primaryFields = new Field[]{new Field("Lunch", "offer", "20% off")};
    Field[] auxiliaryFields = new Field[]{new Field("EXPIRES", "expires", "2 weeks")};
    Field[] backFields = new Field[]{new Field("TERMS AND CONDITIONS", "terms", "Generico offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"as is,\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licensors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. "
            + "Generico reserves the right to change or discontinue at any time any aspect or feature of the pass.")};
    coupon.setPrimaryFields(Arrays.asList(primaryFields));
    coupon.setAuxiliaryFields(Arrays.asList(auxiliaryFields));
    coupon.setBackFields(Arrays.asList(backFields));
    pass.setCoupon(coupon);
    signPass.signPass(pass, true);
  }
}
