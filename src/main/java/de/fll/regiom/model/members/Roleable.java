package de.fll.regiom.model.members;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonSubTypes({
		@JsonSubTypes.Type(value = Team.class, name = "team"),
		@JsonSubTypes.Type(value = Spectator.class, name = "specator"),
		@JsonSubTypes.Type(value = GenericRoleHolder.class, name = "roleHolder")
})
public interface Roleable {

	@JsonIgnore
	@NotNull
	List<Long> getRoles();
}
