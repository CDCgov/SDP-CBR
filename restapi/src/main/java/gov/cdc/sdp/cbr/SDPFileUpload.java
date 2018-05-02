package gov.cdc.sdp.cbr;

import org.apache.camel.Exchange;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;

import java.util.List;

public class SDPFileUpload extends FileUpload {

	public SDPFileUpload() {
		super();
	}

	public SDPFileUpload(FileItemFactory fileItemFactory) {
		super(fileItemFactory);
	}

	public List<FileItem> parseInputStream(Exchange exchange) throws FileUploadException {
		return parseRequest(new SDPRequestContext(exchange));
	}
}