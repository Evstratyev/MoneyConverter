package com.example.orin1.moneyconverter.base;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends BaseView> {
  private WeakReference<V> viewRef;

  final void attachView(V view) {
    viewRef = new WeakReference<>(view);
    onAttach();
  }

  final void detachView() {
    onDetach();
    viewRef.clear();
  }

  protected void onAttach() { }

  protected void onDetach() { }

  public final V getView() {
    return viewRef.get();
  }
}
