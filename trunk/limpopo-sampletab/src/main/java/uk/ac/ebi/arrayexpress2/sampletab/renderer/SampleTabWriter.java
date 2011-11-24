package uk.ac.ebi.arrayexpress2.sampletab.renderer;

import java.io.IOException;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;

public class SampleTabWriter extends Writer {
    private Writer writer;
    private MSIWriter msiwriter;
    private SCDWriter scdwriter;

    private Logger log = LoggerFactory.getLogger(getClass());

	public SampleTabWriter(Writer writer) {
        this.writer = writer;
        getLog().debug("making MSIWriter");
        this.msiwriter = new MSIWriter(writer);
        getLog().debug("making SCDWriter");
        this.scdwriter = new SCDWriter(writer);
	}

    protected Logger getLog() {
        return log;
    }

	@Override
    public void flush() throws IOException {
        writer.flush();
    }

	@Override
    public void close() throws IOException {
        writer.close();
    }

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
	}
    
    public void write(SampleData sampledata) throws IOException{
    	msiwriter.write(sampledata.msi);
    	writer.write("\n");
    	scdwriter.write(sampledata.scd);
        this.close();
    }
}
