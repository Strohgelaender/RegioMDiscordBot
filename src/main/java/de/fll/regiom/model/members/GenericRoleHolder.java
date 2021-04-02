package de.fll.regiom.model.members;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
public class GenericRoleHolder implements Roleable {

	private final List<Long> roles;

	@JsonCreator
	public GenericRoleHolder(@JsonProperty("roles") List<Long> roles) {
		this.roles = roles;
	}

	@Override
	public @NotNull List<Long> getRoles() {
		return roles;
	}

	@Override
	public String toString() {
		return "RoleHolder[" + roles + ']';
	}
}
