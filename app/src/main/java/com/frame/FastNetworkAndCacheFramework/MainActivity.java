package com.frame.FastNetworkAndCacheFramework;

import android.app.Activity;

import com.frame.FastNetworkAndCacheFramework.data.FNFDataService;
import com.frame.FastNetworkAndCacheFramework.data.entity.CompositeData;
import com.frame.FastNetworkAndCacheFramework.data.response.StudentOfPostResponse;
import com.frame.FastNetworkAndCacheFramework.data.response.StudentResponse;
import com.frame.FastNetworkAndCacheFramework.data.utils.UiUtils;

import android.frame.FastNetworkAndCacheFramework.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;

public class MainActivity extends Activity {

  @BindView(R.id.main_tv) TextView mMainTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    normalReq();
    concurrenceReq();
  }

  private void normalReq() {
    FNFDataService.getInstance().getStudent()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            new Action1<StudentResponse>() {
              @Override
              public void call(StudentResponse studentResponse) {
                mMainTv.setText(studentResponse.student.toString());
              }
            },
            new Action1<Throwable>() {
              @Override
              public void call(Throwable throwable) {
                Log.e("FastNF", throwable.getMessage());
                UiUtils.dealError(MainActivity.this, throwable);
              }
            });
  }

  private void concurrenceReq() {
    FNFDataService.getInstance().getStudent()
        .zipWith(FNFDataService.getInstance().getStudentByPost("12306"),
            new Func2<StudentResponse, StudentOfPostResponse, CompositeData>() {
              @Override
              public CompositeData call(StudentResponse studentResponse, StudentOfPostResponse studentResponse2) {
                Log.e("FastNF", Thread.currentThread().getName());
                return new CompositeData(studentResponse2, studentResponse);
              }
            })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            new Action1<CompositeData>() {
              @Override
              public void call(CompositeData compositeData) {
                mMainTv.setText(compositeData.studentResponse.student.toString());
              }
            },
            new Action1<Throwable>() {
              @Override
              public void call(Throwable throwable) {
                Log.e("FastNF", throwable.getMessage());
                UiUtils.dealError(MainActivity.this, throwable);
              }
            });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
