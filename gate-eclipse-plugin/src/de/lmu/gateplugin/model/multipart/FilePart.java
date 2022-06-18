package de.lmu.gateplugin.model.multipart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import de.lmu.gateplugin.ui.Activator;

public class FilePart implements Part {

	private static final Activator PLUGIN_INSTANCE = Activator.getInstance();

	private String name;
	private File file;

	public FilePart(String name, File file) {
		this.name = name;
		this.file = file;
	}

	@Override
	public List<String> getContentHeaders() {
		String contentDisposition = "Content-Disposition: form-data; name=\"" + name + "\"; filename=\""
				+ file.getName() + "\"";
		String contentType = "Content-Type: " + getMimeType().orElse("application/octet-stream");

		return Arrays.asList(new String[] { contentDisposition, contentType });
	}

	private Optional<String> getMimeType() {
		String mimeType = null;
		try {
			mimeType = Files.probeContentType(file.toPath());
		} catch (IOException e) {
			PLUGIN_INSTANCE.getLogger().warn("Exception while probing content type of file: " + file);
		}
		if (mimeType == null) {
			mimeType = URLConnection.guessContentTypeFromName(file.getName());
		}
		return Optional.ofNullable(mimeType);
	}

	@Override
	public Supplier<InputStream> getContentStream() {
		return () -> createInputStreamFromFile();
	}

	private FileInputStream createInputStreamFromFile() {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
