package de.lmu.gateplugin.model.multipart;

import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;

public interface Part {

	List<String> getContentHeaders();

	Supplier<InputStream> getContentStream();

}
