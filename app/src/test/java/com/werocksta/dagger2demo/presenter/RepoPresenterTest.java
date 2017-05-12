package com.werocksta.dagger2demo.presenter;

import com.werocksta.dagger2demo.api.ApiService;
import com.werocksta.dagger2demo.model.RepoCollection;
import com.werocksta.dagger2demo.util.RxSchedulersOverrideRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepoPresenterTest {

    @Mock
    ApiService service;

    @Mock
    RepoPresenter.View view;

    @Rule
    public RxSchedulersOverrideRule mRxSchedulersRule = new RxSchedulersOverrideRule();

    RepoPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new RepoPresenter(service);
        presenter.injectView(view);
    }

    @Test
    public void presenterShouldNotNull() throws Exception {
        assertNotNull(presenter);
    }

    @Test
    public void getRepoShouldDisplayListRepo() throws Exception {
        List<RepoCollection> collections = new ArrayList<>();
        when(service.getRepo("WeRockStar")).thenReturn(Observable.just(collections));
        presenter.getRepo("WeRockStar");
        verify(view).loading();
        verify(view).displayRepo(collections);
        verify(view).loadComplete();
    }

    @Test
    public void getRepoErrorShouldReturnException() throws Exception {
        Throwable exception = new Throwable();
        when(service.getRepo("WeRockStar")).thenReturn(Observable.error(exception));
        presenter.getRepo("WeRockStar");
        verify(view).loading();
        verify(view).getRepoError(exception.getMessage());
        verify(view).loadComplete();
    }

    @After
    public void tearDown() throws Exception {
        presenter.onStop();
    }
}