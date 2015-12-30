package com.frame.FastNetworkAndCacheFramework.data;

import com.frame.FastNetworkAndCacheFramework.data.response.StudentResponse;

import retrofit.http.GET;

interface HmRestService {

  @GET("/6dc2acac00f4770a4093/raw/d5dbfe8f08ae24eceefad6ee0c53e8e2d9647b3b/gistfile1.txt")
  StudentResponse getStudent();

}
