package com.frame.FastNetworkAndCacheFramework.data;

import android.frame.FastNetworkAndCacheFramework.BuildConfig;
import android.util.Log;

import com.frame.FastNetworkAndCacheFramework.data.common.ShowDialogError;
import com.frame.FastNetworkAndCacheFramework.data.common.TokenFailedError;
import com.frame.FastNetworkAndCacheFramework.data.response.BaseResponse;
import com.frame.FastNetworkAndCacheFramework.data.response.StudentOfPostResponse;
import com.frame.FastNetworkAndCacheFramework.data.response.StudentResponse;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;


import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class FNFDataService {

  private static final String RELEASE_ENDPOINT = "https://gist.githubusercontent.com/shuhuaxie";
  private static final String TEST_ENDPOINT = "https://gist.githubusercontent.com/shuhuaxie";
  private static final long CONNECT_TIMEOUT_MILLIS = 20 * 1000;
  private static final long READ_TIMEOUT_MILLIS = 30 * 1000;

  private static FNFDataService sInstance;

  public static FNFDataService getInstance() {
    if (sInstance == null) {
      sInstance = new FNFDataService(DataManager.getInstance());
    }
    return sInstance;
  }

  private final DataManager mDataManager;
  private final FNFRestService mHmRestService;

  public FNFDataService(DataManager dataManager) {
    mDataManager = dataManager;
    Gson gson = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .disableHtmlEscaping()
        .create();
    GsonConverter gsonConverter = new GsonConverter(gson);
    OkHttpClient client = new OkHttpClient();
    client.setConnectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    client.setReadTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

    RequestInterceptor interceptor = new RequestInterceptor() {
      @Override
      public void intercept(RequestFacade request) {
        request.addHeader("Content-Type", "application/json");
      }
    };

    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint(getEndPoint())
        .setConverter(gsonConverter)
        .setRequestInterceptor(interceptor)
        .setClient(new OkClient(client))
        .build();
    mHmRestService = restAdapter.create(FNFRestService.class);
  }

  public Observable<StudentResponse> getStudent() {
    return Observable.create(new Observable.OnSubscribe<StudentResponse>() {
      @Override
      public void call(Subscriber<? super StudentResponse> subscriber) {
        if (subscriber.isUnsubscribed()) {
          return;
        }
        StudentResponse cachedResp = mDataManager.get(CacheKeys.STUDENT);
        if (cachedResp != null) {
          subscriber.onNext(cachedResp);
          subscriber.onCompleted();
          return;
        }
        StudentResponse response = mHmRestService.getStudent();
        checkErrorMessage(response);
        mDataManager.put(CacheKeys.STUDENT, response);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    }).subscribeOn(Schedulers.io());
  }

  public Observable<StudentOfPostResponse> getStudentByPost(final String id) {
    return Observable.create(new Observable.OnSubscribe<StudentOfPostResponse>() {
      @Override
      public void call(Subscriber<? super StudentOfPostResponse> subscriber) {
        if (subscriber.isUnsubscribed()) {
          return;
        }
        StudentOfPostResponse cachedResp = mDataManager.get(CacheKeys.STUDENT);
        if (cachedResp != null) {
          subscriber.onNext(cachedResp);
          subscriber.onCompleted();
          return;
        }
        StudentOfPostResponse response = mHmRestService.getStudentByPost(
            new FNFDataServiceTasks.GetPersonTask(id));
        checkErrorMessage(response);
        mDataManager.put(CacheKeys.STUDENT, response);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    }).subscribeOn(Schedulers.io());
  }

  public String getEndPoint() {
    switch (BuildConfig.SERVER_ENVIRONMENT) {
      case Release:
        Log.e("FastNF", "server_env : Release");
        return RELEASE_ENDPOINT;
      case Test:
        Log.e("FastNF", "server_env : Test");
        return TEST_ENDPOINT;
      default:
        Log.e("FastNF", "server_env : Default");
        return RELEASE_ENDPOINT;
    }
  }

  private void checkErrorMessage(BaseResponse response) {
    if (response.status == 401) {
      throw new TokenFailedError(response.message);
    } else if (response.status == 901) {
      throw new ShowDialogError(response.message);
    } else if (response.status != 0) {
      throw new RuntimeException(response.message);
    }
  }

}
