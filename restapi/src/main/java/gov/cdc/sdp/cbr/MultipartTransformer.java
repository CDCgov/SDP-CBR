package gov.cdc.sdp.cbr;

import java.io.File;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.StreamCache;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipartTransformer implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(MultipartTransformer.class);

	@Override
	public void process(Exchange exchange) throws Exception {

		Message msg = exchange.getIn();
		//		String contentType = MessageHelper.getContentType(msg);
		//		LOG.info("Message Content Type: {}", MessageHelper.getContentType(msg));
		//		LOG.info("Message Headers as XML {}: \n", MessageHelper.dumpAsXml(msg, true)); // Do dump body

		StreamCache cache = msg.getBody(StreamCache.class);
		// Stream may have already been read, so reset to beginning
		cache.reset();

		//		if (contentType.startsWith(MediaType.MULTIPART_FORM_DATA)) {

		DiskFileItemFactory factory = new DiskFileItemFactory();
		//factory.setSizeThreshold(1000240); // TODO: What is the right size?
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		SDPFileUpload upload = new SDPFileUpload(factory);

		java.util.List<FileItem> items;
		items = upload.parseInputStream(exchange);

		// Here we assume we have only one file, but we could split it here somehow and link them to camel properties or headers...
		// with this, the first file sent with your multipart replaces the body
		// of the exchange for the next processor to handle it
		msg.setBody(items.get(0).getInputStream());


		//		} else {
		//			LOG.error("Did not Receive Multipart message as expected");
		//			// TODO: Throw some kind of exception?
		//		}

	}


}
