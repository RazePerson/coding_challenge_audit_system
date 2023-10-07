package org.example.diff;

import org.example.changetype.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class DiffTool {

    private final DiffService diffService = new DiffService();

    public <T> List<ChangeType> diff(T previous, T current) throws Exception {
        List<ChangeType> changeTypes = new ArrayList<>();
        diffService.handleDiff("", previous, current, changeTypes);
        return changeTypes;
    }
}
