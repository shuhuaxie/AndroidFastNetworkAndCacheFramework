package com.frame.FastNetworkAndCacheFramework.data;

import com.frame.FastNetworkAndCacheFramework.data.response.StudentOfPostResponse;
import com.frame.FastNetworkAndCacheFramework.data.response.StudentResponse;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

interface FNFRestService {

  @GET("/6dc2acac00f4770a4093/raw/d5dbfe8f08ae24eceefad6ee0c53e8e2d9647b3b/gistfile1.txt")
  StudentResponse getStudent();

  @POST("/6dc2acac00f4770a4093/raw/d5dbfe8f08ae24eceefad6ee0c53e8e2d9647b3b/gistfile1.txt")
  StudentOfPostResponse getStudentByPost(
      @Body FNFDataServiceTasks.GetPersonTask task);
}
