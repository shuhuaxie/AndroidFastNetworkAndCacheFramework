package com.frame.FastNetworkAndCacheFramework.data.common;


public class ShowDialogError extends Error {
  public String message;

  public ShowDialogError(String message) {
    super();
    this.message = message;
  }
}
