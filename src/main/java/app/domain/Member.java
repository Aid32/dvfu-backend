package app.domain;

import java.util.ArrayList;
import java.util.List;


public class Member {
    private long id;
    private String name;
    private MembershipType membershipType;
    private List<Long> activeRentals = new ArrayList<>();

    public Member(long id, String name, MembershipType membershipType){
        this.id = id;
        this.name = name;
        this.membershipType = membershipType;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }
    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public List<Long> getActiveRentals() {
        return activeRentals;
    }
    public void setActiveRentals(List<Long> activeRentals) {
        this.activeRentals = activeRentals;
    }
}
