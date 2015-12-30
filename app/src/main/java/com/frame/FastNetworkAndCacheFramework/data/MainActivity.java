package com.frame.FastNetworkAndCacheFramework.data;

import android.app.Activity;
import com.frame.FastNetworkAndCacheFramework.data.response.StudentResponse;

import android.frame.FastNetworkAndCacheFramework.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends Activity {


  @InjectView(R.id.main_tv) TextView mMainTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
    HmDataService.getInstance().getStudent()
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
                Log.e("FastNCF", throwable.getMessage());
              }
            });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
