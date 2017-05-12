package com.werocksta.dagger2demo.presenter;

import com.werocksta.dagger2demo.api.ApiService;
import com.werocksta.dagger2demo.model.GithubUserCollection;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class GithubUserInfoPresenter {

    private View view;
    private ApiService service;
    private CompositeSubscription subscription;

    public interface View {
        void loading();

        void getUserInfoSuccess(GithubUserCollection userInfo);

        void getUserInfoError(String message);

        void getUserInfoComplete();
    }

    public GithubUserInfoPresenter(View view, ApiService service) {
        this.view = view;
        this.service = service;
        this.subscription = new CompositeSubscription();
    }

    public void getUserInfo(String username) {
        view.loading();

        subscription.add(service.getUserInfo(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> view.getUserInfoComplete())
                .subscribe(
                        gitUserInfo -> {
                            view.getUserInfoSuccess(gitUserInfo);
                        },
                        throwable -> {
                            view.getUserInfoError(throwable.getMessage());
                        }
                )
        );
    }


    public void onStop() {
        subscription.clear();
    }
}
