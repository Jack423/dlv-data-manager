package io.apexapps.dlvdatamanager.utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationUtil {

    public static void show(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.setDuration(3000);
        notification.open();
    }

    public static void showSuccess(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setDuration(3000);
        notification.open();
    }

    public static void showError(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(3000);
        notification.open();
    }

    public static void showWarning(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
        notification.setDuration(3000);
        notification.open();
    }
}
