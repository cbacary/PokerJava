public enum Suit {
    HEARTS((int)Math.pow(2, 13)),   // 2^13 = 8192
    DIAMONDS((int)Math.pow(2, 14)), // 2^14 = 16384
    SPADES((int)Math.pow(2, 15)),   // 2^15 = 32768
    CLUBS((int)Math.pow(2, 16));    // 2^16 = 65536

    public static final int ALL_SUITS = 122880;

    private int suit;
    // An integer representation of all the suits. Also, this is why i hate java,
    // in other languages with enums i could just do HEARTS | DIAMONDS .... 
    // but in java you'd have to say HEARTS.getValue() | DIAMONDS.getValue() ...
    // which suddenly turns this into a massive mess and i'd prefer to just 
    // hardcode the value, which now defeats the point of OOP

    Suit(int _suit) {
        suit = _suit;

    }

    public int getValue() {
        return suit;
    }

}

