public enum Rank {

    // Initially i did hand checking quite differently but i decided to keep the 
    // powers of 2 thing anyway even though its only actually utilized once lol. 
    // so it is probably dumb yea

    ONE   (0)                   ,       // 0     -> used for ace
    TWO   ((int) Math.pow(2, 1)),       // 2^1
    THREE ((int) Math.pow(2, 2)),       // 2^2
    FOUR  ((int) Math.pow(2, 3)),       // 2^3
    FIVE  ((int) Math.pow(2, 4)),       // 2^4
    SIX   ((int) Math.pow(2, 5)),       // 2^5
    SEVEN ((int) Math.pow(2, 6)),       // 2^6
    EIGHT ((int) Math.pow(2, 7)),       // 2^7
    NINE  ((int) Math.pow(2, 8)),       // 2^8
    TEN   ((int) Math.pow(2, 9)),       // 2^9
    JACK  ((int) Math.pow(2, 10)),      // 2^10
    QUEEN ((int) Math.pow(2, 11)),      // 2^11
    KING  ((int) Math.pow(2, 12)),      // 2^12
    ACE   ((int) Math.pow(2, 13));      // 2^13

    public static final int ALL_RANKS = 8191;

    private int rank;

    Rank(int _rank) { rank = _rank; }

    public int getValue() { return rank; }
}
