package de.fll.regiom.model;

public class Team {

	private final String name;
	private final int hotID;
	private long roleID;

	public Team(String name, int hotID) {
		this.name = name;
		this.hotID = hotID;
	}

	public Team(String name, long roleID, int hotID) {
		this(name, hotID);
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
