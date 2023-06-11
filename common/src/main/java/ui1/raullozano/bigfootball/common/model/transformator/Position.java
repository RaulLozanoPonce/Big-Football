package ui1.raullozano.bigfootball.common.model.transformator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum Position {

    PT(0), DF(1), CC(2), DL(3);

    private final Integer priority;

    Position(int priority) {
        this.priority = priority;
    }

    public static Set<Position> positionOf(String positions) {
        try {
            return Arrays.stream(positions.split(",")).map(Position::singlePositionOf).collect(Collectors.toSet());
        } catch(Throwable t) {
            t.printStackTrace();
            return new HashSet<>();
        }
    }

    public static Position singlePositionOf(String position) {
        switch (position) {
            case "GK":
                return PT;
            case "CB":
            case "LB":
            case "RB":
            case "WB":
            case "DF":
                return DF;
            case "DM":
            case "CM":
            case "MF":
            case "AM":
            case "LM":
            case "RM":
                return CC;
            case "LW":
            case "RW":
            case "FW":
                return DL;
            default:
                return null;
        }
    }

    public static Position firstPositionOf(String positions) {
        return singlePositionOf(Arrays.asList(positions.split(",")).get(0));
    }

    public static int compare(String p1, String p2) {
        try {
            return firstPositionOf(p1).priority.compareTo(firstPositionOf(p2).priority);
        } catch(Throwable t) {
            return 0;
        }
    }

    public static int compare(Position p1, Position p2) {
        try {
            return p1.priority.compareTo(p2.priority);
        } catch(Throwable t) {
            return 0;
        }
    }
}