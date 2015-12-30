package com.frame.FastNetworkAndCacheFramework.data;

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

public class HmDataService {

  private static final String ENDPOINT = "https://gist.githubusercontent.com/shuhuaxie";
  private static final long CONNECT_TIMEOUT_MILLIS = 20 * 1000;
  private static final long READ_TIMEOUT_MILLIS = 30 * 1000;

  private static HmDataService sInstance;

  public static HmDataService getInstance() {
    if (sInstance == null) {
      sInstance = new HmDataService(DataManager.getInstance());
    }
    return sInstance;
  }

  private final DataManager mDataManager;
  private final HmRestService mHmRestService;

  public HmDataService(DataManager dataManager) {
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
    mHmRestService = restAdapter.create(HmRestService.class);
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
        mDataManager.put(CacheKeys.STUDENT, response);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    }).subscribeOn(Schedulers.io());
  }
  public String getEndPoint() {
    return ENDPOINT;
  }

}
