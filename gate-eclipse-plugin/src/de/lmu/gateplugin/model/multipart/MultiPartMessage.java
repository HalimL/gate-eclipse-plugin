package de.lmu.gateplugin.model.multipart;

import java.util.ArrayList;
import java.util.List;

public class MultiPartMessage {

	private List<Part> parts = new ArrayList<>();

	public void addPart(Part part) {
		parts.add(part);
	}

	public List<Part> getParts() {
		return new ArrayList<>(parts);
	}

}
