package ru.nsu.fit.endpoint.shared;

import ru.nsu.fit.endpoint.service.database.DBService;
import ru.nsu.fit.endpoint.service.database.data.Subscription;

/**
 * Author: Alexander Fal (falalexandr007@gmail.com)
 */
public class ExternalSubscriptionHandler implements Runnable {
    private Subscription subscription;

    public void run() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        	System.out.println("updating subscription " + subscription.getId()+ " status");
            subscription.getData().setStatus(Subscription.SubscriptionData.Status.DONE);
            System.out.print(subscription.getData());
            DBService.updateSubscription(subscription);
        }
    }

    public ExternalSubscriptionHandler(Subscription subscription) { this.subscription = subscription; }
}
