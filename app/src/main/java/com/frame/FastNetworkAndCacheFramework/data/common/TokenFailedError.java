package com.frame.FastNetworkAndCacheFramework.data.common;


public class TokenFailedError extends Error {
  public String message;

  public TokenFailedError( String message) {
    super();
    this.message = message;
  }
}
