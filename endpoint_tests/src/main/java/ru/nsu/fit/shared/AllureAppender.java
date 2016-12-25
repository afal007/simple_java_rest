package ru.nsu.fit.shared;

import org.apache.log4j.spi.LoggingEvent;

/**
 * author: Alexander Fal (falalexandr007@gmail.com)
 */
public class AllureAppender extends org.apache.log4j.AppenderSkeleton {
    @Override
    protected void append(LoggingEvent event) {
        /*String message;
        if(event.locationInformationExists()){
            StringBuilder formatedMessage = new StringBuilder();
            formatedMessage.append(event.getLocationInformation().getClassName());
            formatedMessage.append(".");
            formatedMessage.append(event.getLocationInformation().getMethodName());
            formatedMessage.append(":");
            formatedMessage.append(event.getLocationInformation().getLineNumber());
            formatedMessage.append(" - ");
            formatedMessage.append(event.getMessage().toString());
            message = formatedMessage.toString();
        } else {
            message = event.getMessage().toString();
        }

        AllureUtils.saveTextLog(message);*/
        AllureUtils.saveTextLog("Header:", event.getMessage().toString());
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
