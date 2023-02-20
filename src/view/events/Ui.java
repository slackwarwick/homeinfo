package view.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple UI event-bus
 */
public class Ui {

    private final Map<Class<? extends Event>, List<Consequence>> consequences = new HashMap<>();

    public void notify(Event event) {
        List<Consequence> cc = consequences.get(event.getClass());
        if (cc != null)
            for (Consequence c : cc)
                c.occured(event);
    }

    public void bind(Class<? extends Event> e, Consequence c) {
        if (consequences.get(e) == null)
            consequences.put(e, new ArrayList<>());
        consequences.get(e).add(c);
    }
}
