package com.seoul.publicbooksearcher.domain.usecase;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a {@link rx.Subscriber}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
public abstract class UseCase<T>{

  private Subscription subscription = Subscriptions.empty();

  /**
   * Builds an {@link rx.Observable} which will be used when executing the current {@link UseCase}.
   */
  protected abstract Observable buildUseCaseObservable(T t);

  /**
   * Executes the current use case.
   *
   * @param useCaseSubscriber The guy who will be listen to the observable build
   * with {@link #buildUseCaseObservable(T t)}.
   */
  @SuppressWarnings("unchecked")
  public void execute(T t, Subscriber useCaseSubscriber) {
    this.subscription = this.buildUseCaseObservable(t)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(useCaseSubscriber);
  }

  /**
   * Unsubscribes from current {@link rx.Subscription}.
   */
  public void unsubscribe() {
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }
}