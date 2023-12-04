public enum Rank {
    // It probably looks unnececary to have these be equal to powers of 2, but 
    // it makes writing performant hand checks easeir. 
    NONE(0),
    TWO((int)Math.pow(2, 0)),       // 2^0 = 1
    THREE((int)Math.pow(2, 1)),     // 2^1 = 2
    FOUR((int)Math.pow(2, 2)),      // 2^2 = 4
    FIVE((int)Math.pow(2, 3)),      // 2^3 = 8
    SIX((int)Math.pow(2, 4)),       // 2^4 = 16
    SEVEN((int)Math.pow(2, 5)),     // 2^5 = 32
    EIGHT((int)Math.pow(2, 6)),     // 2^6 = 64
    NINE((int)Math.pow(2, 7)),      // 2^7 = 128
    TEN((int)Math.pow(2, 8)),       // 2^8 = 256
    JACK((int)Math.pow(2, 9)),      // 2^9 = 512
    QUEEN((int)Math.pow(2, 10)),    // 2^10 = 1024
    KING((int)Math.pow(2, 11)),     // 2^11 = 2048
    ACE((int)Math.pow(2, 12));      // 2^12 = 4096

    public static final int ALL_RANKS = 8191;

    private int rank;

    Rank(int _rank) {
        rank = _rank;
    }

    public int getValue() {
        return rank;
    }   
}
