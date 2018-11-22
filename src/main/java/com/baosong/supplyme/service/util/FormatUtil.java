package com.baosong.supplyme.service.util;

import com.baosong.supplyme.domain.User;

/**
 * Utility class for formatting entity data.
 */
public final class FormatUtil {

    public static String formatUser(User user) {
        if (user == null) {
            return "";
        } else {
            return String.format("%s %s", user.getLastName(), user.getFirstName()).trim();
        }
    }
}
