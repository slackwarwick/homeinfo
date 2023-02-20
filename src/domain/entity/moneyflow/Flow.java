package domain.entity.moneyflow;

import java.util.ArrayList;
import java.util.List;

public class Flow {
    private final FlowCode code;
    private final List<Tag> tags = new ArrayList<>();

    public Flow(FlowCode code) {
        this.code = code;
    }

    public FlowCode code() {
        return this.code;
    }

    public List<Tag> tags() {
        return tags;
    }
}
