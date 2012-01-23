package uk.ac.ebi.arrayexpress2.sampletab.renderer;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SCD;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SCDNode;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.scd.node.SampleNode;
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
	}

    public void write(SCD scd) throws IOException{
    	writer.write("[SCD]\n");
    	SCDTableBuilder tb = new SCDTableBuilder(scd.getRootNodes());
		log.debug("Starting to assemble table...");
    	List<List<String>> table = tb.getTable(); 
		log.debug("Table assembled");
        for (List<String> row : table) {
        	for (String entry : row){
    			if (entry != null){
    				writer.write(entry);
    			}
    	    	writer.write("\t");        		
        	}
	    	writer.write("\n");
        }
    }
}
