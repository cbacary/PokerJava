public enum Suit {
    HEARTS(0),
    DIAMONDS(1), 
    SPADES(2),   
    CLUBS(3);    

    private int suit;

    Suit(int _suit) {
        suit = _suit;

    }

    public int getValue() {
        return suit;
    }

}

