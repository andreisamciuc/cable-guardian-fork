package co.uk.vonaq.cableguardian.core;

import co.uk.vonaq.cableguardian.Inject.Log;
import co.uk.vonaq.cableguardian.messages.MessageReceiver;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

public class MessageThread extends Thread {
    //private static final int iterationTime = 5;//seconds
    @Log
    private Logger logger;
    @Inject
    private MessageReceiver messageReceiver;
//    @Inject
//    private Sleeper sleeper;

    public void run() {
        logger.log(Level.INFO, "Messages loop is started");
        Thread.currentThread().setName("InboxThread");
        while (true) {
            try {
                messageReceiver.checkSMS();
            } catch (Exception e) {
                logger.log(Level.WARNING, "error with sms.", e);
            }
            try {
                messageReceiver.checkEmail();
            } catch (Exception e) {
                logger.log(Level.WARNING, "error with email.", e);
            }
            try {
                messageReceiver.checkProcessedOutboxSMS();
            } catch (Exception e) {
                logger.log(Level.WARNING, "error with sms.", e);
            }
            //sleeper.sleepResponsibly(iterationTime);
            logger.log(Level.INFO, "message thread iteration---------");
            try {Thread.sleep(5000l);}catch(Exception e){}
        }
    }
}
