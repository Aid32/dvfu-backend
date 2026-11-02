package app.domain;

public class Equipment {
    private long id;
    private String name;
    private String type;
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;
    private Long rentedBy;

    public Equipment(long id, String name, String type){
        this.id = id;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public EquipmentStatus getStatus() {
        return status;
    }
    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public Long getRentedBy() {
        return rentedBy;
    }
    public void setRentedBy(Long id) {
        this.rentedBy = id;
    }
}
