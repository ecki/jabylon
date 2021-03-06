package de.jutzig.jabylon.index.properties.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.cdo.common.id.CDOIDUtil;
import org.eclipse.emf.common.notify.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jutzig.jabylon.index.properties.IndexActivator;
import de.jutzig.jabylon.index.properties.QueryService;
import de.jutzig.jabylon.properties.PropertyFileDescriptor;
import de.jutzig.jabylon.resources.changes.PropertiesListener;

@Component
@Service
public class PropertyIndex extends Job implements PropertiesListener {

	private static final Logger logger = LoggerFactory.getLogger(PropertyIndex.class);
	BlockingQueue<DocumentTuple> writes;

	public PropertyIndex() {
		super("Index Job");
		writes = new ArrayBlockingQueue<DocumentTuple>(50);
	}

	protected IndexWriter createIndexWriter() throws CorruptIndexException, LockObtainFailedException, IOException {
		Directory directory = IndexActivator.getDefault().getOrCreateDirectory();

		IndexWriterConfig c = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
		
		return new IndexWriter(directory, c);
	}

	@Override
	public void propertyFileAdded(PropertyFileDescriptor descriptor, boolean autoSync) {

		PropertyFileAnalyzer analyzer = new PropertyFileAnalyzer();
		List<Document> documents = analyzer.createDocuments(descriptor);
		try {
			writes.put(new DocumentTuple(documents));
			schedule();
		} catch (InterruptedException e) {
			logger.warn("Interrupted",e);
		}

	}

	@Override
	public void propertyFileDeleted(PropertyFileDescriptor descriptor, boolean autoSync) {
		try {
			writes.put(new DocumentTuple(descriptor));
			schedule();
		} catch (InterruptedException e) {
			logger.warn("Interrupted",e);
		}

	}

	@Override
	public void propertyFileModified(PropertyFileDescriptor descriptor, List<Notification> changes, boolean autoSync) {
		PropertyFileAnalyzer analyzer = new PropertyFileAnalyzer();
		List<Document> documents = analyzer.createDocuments(descriptor);
		try {
			writes.put(new DocumentTuple(descriptor, documents));
			schedule();
		} catch (InterruptedException e) {
			logger.warn("Interrupted",e);
		}

	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IndexWriter writer = null;
		try {
			writer = createIndexWriter();
			while (true) {
				DocumentTuple documentTuple = writes.poll();
				if (documentTuple == null)
					break;
				List<Document> documents = documentTuple.getDocuments();
				switch (documentTuple.getAction()) {
				case CREATE:
					for (Document document : documents) {
						writer.addDocument(document);
					}
					break;
				case DELETE:
					StringBuilder builder = new StringBuilder();
					CDOIDUtil.write(builder, documentTuple.getDescriptor().cdoID());
					writer.deleteDocuments(new Term(QueryService.FIELD_CDO_ID, builder.toString()));
					break;
				case REPLACE:
					writer.deleteDocuments(new Term(QueryService.FIELD_FULL_PATH, documentTuple.getDescriptor().fullPath().toString()));
					for (Document document : documents) {
						writer.addDocument(document);
					}
					break;

				default:
					break;
				}

			}
			writer.commit();

		} catch (CorruptIndexException e) {
			logger.error("Exception while indexing",e);
		} catch (LockObtainFailedException e) {
			logger.error("Exception while indexing",e);
		} catch (IOException e) {
			logger.error("Exception while indexing",e);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				logger.error("Exception while closing index writer",e);
			} catch (IOException e) {
				logger.error("Exception while closing index writer",e);
			}
		}

		return Status.OK_STATUS;
	}

	@Override
	public boolean belongsTo(Object family) {
		return IndexWriter.class == family;
	}

}

class DocumentTuple {
	private List<Document> docs;
	private DocumentAction action;
	private PropertyFileDescriptor descriptor;

	public DocumentTuple(List<Document> docs) {
		super();
		this.docs = docs;
		this.action = DocumentAction.CREATE;
	}

	public DocumentTuple(PropertyFileDescriptor descriptor) {
		super();
		this.descriptor = descriptor;
		this.action = DocumentAction.DELETE;
	}

	public DocumentTuple(PropertyFileDescriptor descriptor, List<Document> docs) {
		super();
		this.descriptor = descriptor;
		this.docs = docs;
		this.action = DocumentAction.REPLACE;
	}

	public DocumentAction getAction() {
		return action;
	}

	public List<Document> getDocuments() {
		return docs;
	}

	public PropertyFileDescriptor getDescriptor() {
		return descriptor;
	}

}

enum DocumentAction {
	CREATE, DELETE, REPLACE;
}
