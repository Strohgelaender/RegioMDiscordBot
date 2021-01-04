package de.fll.regiom.model;

import java.util.Collections;
import java.util.List;

public class Spectator implements RoleHolding {

	@Override
	public List<Long> getRoles() {
		return Collections.singletonList(Constants.SPECTATOR_ROLE_ID);
	}
}
