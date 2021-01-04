package de.fll.regiom.model;

public class Team {

    private final String name;
    private long roleID;

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, long roleID) {
        this.name = name;
        this.roleID = roleID;
    }

    public String getName() {
        return name;
    }

    public long getRoleID() {
        return roleID;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }
}
