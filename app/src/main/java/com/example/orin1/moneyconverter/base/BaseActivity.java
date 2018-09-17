package com.example.orin1.moneyconverter.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<V extends BaseView, P extends BasePresenter<V>> extends AppCompatActivity {
  private P presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    attachPresenter();
  }

  @SuppressWarnings("unchecked")
  private void attachPresenter() {
    //Fragment f = new Fragment();
    //f.setRetainInstance(true);
    //f.setObject(onRetainCustomNonConfigurationInstance());
    //getSupportFragmentManager().beginTransaction().add(f, "TAG").commit();
    //
    //Fragment savedFragment = getSupportFragmentManager().findFragmentByTag("TAG");
    //savedFragment.getObject();
    P presenter = (P) getLastCustomNonConfigurationInstance();
    if (presenter == null) {
      presenter = providePresenter();
    }
    presenter.attachView((V) this);
    this.presenter = presenter;
  }

  @Override
  protected void onDestroy() {
    presenter.detachView();
    super.onDestroy();
  }

  @Override
  public Object onRetainCustomNonConfigurationInstance() {
    return presenter;
  }

  protected abstract P providePresenter();

  protected final P getPresenter() {
    return presenter;
  }
}
