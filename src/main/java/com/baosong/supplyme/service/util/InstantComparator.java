package com.baosong.supplyme.service.util;

import java.time.Instant;
import java.util.Comparator;

/**
 * InstantComparator
 */
public class InstantComparator implements Comparator<Instant> {

    @Override
    public int compare(Instant o1, Instant o2) {
        if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else {
            return o1.compareTo(o2);
        }
    }
}
