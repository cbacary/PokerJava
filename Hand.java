public enum Hand {
    HIGH_CARD(1),
    PAIR(2),
    TWO_PAIR(3),
    THREE_KIND(4),
    STRAIGHT(5),
    FLUSH(6),
    FULL_HOUSE(7),
    FOUR_KIND(8),
    STRAIGHT_FLUSH(9),
    ROYAL_FLUSH(10);

    private final int value;

    Hand(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }
    
}
