package uk.ac.ebi.arrayexpress2.sampletab.renderer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.renderer.scd.SCDNodeFactory;
import uk.ac.ebi.arrayexpress2.sampletab.renderer.scd.SCDTableBuilder;

public class SCDWriter extends Writer {
    private Writer writer;

    private Logger log = LoggerFactory.getLogger(getClass());

	public SCDWriter(Writer writer) {
        this.writer = writer;
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
	    throw new NotImplementedException();
	}

    public void write(SCD scd) throws IOException {
        writer.write("[SCD]\n");
        List<List<String>> table;
        
        //need to ensure only one thread is doing this at a time
        synchronized(SCDNodeFactory.class) {
        	SCDTableBuilder tb = new SCDTableBuilder(scd.getRootNodes());
    		log.debug("Starting to assemble table...");
        	table = tb.getTable(); 
    		log.debug("Table assembled");
    		
            //recreate the node factory to flush its internal cache
    		SCDNodeFactory.clear();
        }
        
        //now we can write to disk at leisure
        for (List<String> row : table) {
            for (String value : row) {
                if (value != null) {
                    value = SampleTabWriter.sanitize(value);
                    
                    writer.write(value);
                }
                writer.write("\t");             
            }
            writer.write("\n");
        }
    }
}
