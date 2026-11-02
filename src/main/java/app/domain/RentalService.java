package app.domain;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


public class RentalService {
    private final Map<Long, Equipment> equipment = new LinkedHashMap<>();
    private final Map<Long, Member> members = new LinkedHashMap<>();
    private final AtomicLong equipmentSeq = new AtomicLong(1);
    private final AtomicLong membersSeg = new AtomicLong(1);

    public synchronized Equipment createEquipment(String name, String type) {
        if (name == null || name.isBlank() || type == null || type.isBlank())
            throw new DomainException(400, "name and type required");
        Equipment e = new Equipment(equipmentSeq.getAndIncrement(), name, type);
        equipment.put(e.getId(), e);
        return e;
    }

    public synchronized List<Equipment> listEquipment(EquipmentStatus status, String type) {
        List<Equipment> result = new ArrayList<>();
        for (Equipment e: equipment.values()) {
            if (status != null && e.getStatus() != status) continue;
            if (type != null && !type.equalsIgnoreCase(e.getType())) continue;
            result.add(e);
        }
        return result;
    }

    public synchronized Equipment getEquipment(long id) {
        Equipment e = equipment.get(id);
        if (e == null) throw new DomainException(404, "equipment not found:" + id);
        return e;
    }

    public synchronized Equipment updateEquipment(long id, String name, String type, EquipmentStatus status) {
        Equipment e = getEquipment(id);
        if (status == EquipmentStatus.RENTED && e.getStatus() != EquipmentStatus.RENTED)
            throw new DomainException(409, "status RENTED can only be set via rental operation");
        if (name != null) e.setName(name);
        if (type != null) e.setType(type);
        if (status != null) e.setStatus(status);
        return e;
    }

    public synchronized void deleteEquipment(long id) {
        Equipment e = getEquipment(id);
        if (e.getStatus() == EquipmentStatus.RENTED) throw new DomainException(409, "cannot delete rented equipment");
        equipment.remove(id);
    }

    public synchronized Member createMember(String name, MembershipType type) {
        if (name == null || name.isBlank() || type == null) throw new DomainException(400, "name and MembershipType required");
        Member m =new Member(membersSeg.getAndIncrement(), name, type);
        members.put(m.getId(), m);
        return m;
    }

    public synchronized List<Member> listMembers(MembershipType type) {
        List<Member> result = new ArrayList<>();
        for (Member m : members.values()) {
            if (type != null && m.getMembershipType() != type) continue;
            result.add(m);
        }
        return result;
    }

    public synchronized Member getMember(long id) {
        Member m = members.get(id);
        if (m == null) throw new DomainException(404, "member not found" + id);
        return m;
    }

    public synchronized Member updateMember(long id, String name, MembershipType type) {
        Member m = getMember(id);
        if (type != null && type != m.getMembershipType()) {
            if (m.getActiveRentals().size() > type.maxRentals) throw new DomainException(409, "cannot downgrade: too many active rentals");
            m.setMembershipType(type);
        }
        if (name != null) m.setName(name);
        return m;
    }

    public synchronized void deleteMember(long id) {
        Member m = getMember(id);
        if (!m.getActiveRentals().isEmpty()) throw new DomainException(409, "cannot delete member with active rentals");
        members.remove(id);
    }

    public synchronized void rent(long memberId, long equipmentId) {
        Member m = getMember(memberId);
        Equipment e = getEquipment(equipmentId);
        if (e.getStatus() != EquipmentStatus.AVAILABLE) throw new DomainException(409, "equipment is not available");
        if (m.getActiveRentals().size() >= m.getMembershipType().maxRentals) throw new DomainException(409, "rental limit exceeded");
        e.setStatus(EquipmentStatus.RENTED);
        e.setRentedBy(memberId);
        m.getActiveRentals().add(equipmentId);
    }

    public synchronized void returnEquipment(long memberId, long equipmentId) {
        Member m =getMember(memberId);
        Equipment e = getEquipment(equipmentId);
        if (e.getStatus() != EquipmentStatus.RENTED || !Objects.equals(e.getRentedBy(), memberId))
            throw new DomainException(409, "equipment is not rented by this member");
        e.setStatus(EquipmentStatus.AVAILABLE);
        e.setRentedBy(null);
        m.getActiveRentals().remove(equipmentId);
    }

}
