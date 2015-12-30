package com.frame.FastNetworkAndCacheFramework.data.entity;

import com.frame.FastNetworkAndCacheFramework.data.response.StudentOfPostResponse;
import com.frame.FastNetworkAndCacheFramework.data.response.StudentResponse;

/**
 * Created by xie on 15/12/30.
 */
public class CompositeData {
  public StudentOfPostResponse studentOfPostResponse;
  public StudentResponse studentResponse;

  public CompositeData(StudentOfPostResponse studentOfPostResponse, StudentResponse studentResponse) {
    this.studentOfPostResponse = studentOfPostResponse;
    this.studentResponse = studentResponse;
  }
}
