package app.domain;

public enum MembershipType {
    BASIC(2, 7),
    PREMIUM(5, 30);

    public final int maxRentals;
    public final int rentalDays;

    MembershipType(int maxRentals, int rentalDays){
        this.maxRentals = maxRentals;
        this.rentalDays = rentalDays;
    }


}
