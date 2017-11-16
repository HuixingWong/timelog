package com.example.dogoodsoft_app.timelog.dummy;

import com.example.dogoodsoft_app.timelog.modols.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {


    private static final String [] logNames = new String[]{"编程","读书","魔术"};

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Log> ITEMS = new ArrayList<Log>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, Log> ITEM_MAP = new HashMap<Integer, Log>();


    static {
        // Add some sample items.
        for (int i = 1; i <= logNames.length+1; i++) {

            Log log = new Log();
            log.setCounTime(0l);
            log.setId(i);
            log.setName(logNames[i-1]);
            log.setStartTime( System.currentTimeMillis());

        }
    }

    private static void addItem(Log item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }
}
