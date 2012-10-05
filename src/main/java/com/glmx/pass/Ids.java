package com.glmx.pass;

import java.security.SecureRandom;

public class Ids {
  private static final SecureRandom RANDOM = new SecureRandom();

  public static long nextId() {
    return nextPostiveLong();
  }

  public static String nextBase36Id() {
    return Long.toString(nextId(), 36);
  }

  public static String nextShortBase36Id() {
    return Integer.toString(Math.abs(RANDOM.nextInt()), 36);
  }

  public static byte[] nextBytes() {
    byte[] bytes = new byte[16];
    RANDOM.nextBytes(bytes);
    return bytes;
  }

  /**
   * Get a positive long by looping until we
   * get a positive one
   * @return a positive long secure random
   */
  private static long nextPostiveLong() {
    long id = RANDOM.nextLong();
    while (id < 0) {
      id = RANDOM.nextLong();
    }
    return id;
//    return Double.doubleToRawLongBits(RANDOM.nextDouble() * (Long.MAX_VALUE));
  }
}
