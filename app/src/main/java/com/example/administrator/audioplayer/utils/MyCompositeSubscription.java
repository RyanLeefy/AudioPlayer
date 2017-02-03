package com.example.administrator.audioplayer.utils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by on 2017/2/3.
 */

public class MyCompositeSubscription {

    public static void unsubscribeIfNotNull(Subscription subscription){
        if(subscription != null){
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription){
        if(subscription == null || subscription.isUnsubscribed()){
            return new CompositeSubscription();
        }
        return subscription;
    }
}
