package uk.ac.ebi.arrayexpress2.sampletab.renderer;

import java.io.IOException;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.exception.ValidateException;
import uk.ac.ebi.arrayexpress2.magetab.validator.Validator;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.SampleData;

public class SampleTabWriter extends Writer {
    private final Writer writer;
    private final MSIWriter msiwriter;
    private final SCDWriter scdwriter;

    private Logger log = LoggerFactory.getLogger(getClass());

    public SampleTabWriter(Writer writer) {
        this.writer = writer;
        this.msiwriter = new MSIWriter(writer);
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
    
    public static String sanitize(String value){
        //purge all strange characters not-quite-whitespace
        //note, you can find these unicode codes by pasting u"the character" into python
        value = value.replace("\"", "");
        value = value.replace("\n", "");
        value = value.replace("\t", "");
        value = value.replace("\u2009", " "); //thin space
        value = value.replace("\u00A0", " "); //non-breaking space
        value = value.replace("\uff09", ") "); //full-width right parenthesis
        value = value.replace("\uff08", " ("); //full-width left parenthesis
        
        //strip other non-XML valid control characters
        value = stripNonValidXMLCharacters(value);
        
        return value;
    }
    
    public static String stripNonValidXMLCharacters(String in) {
        //from http://blog.mark-mclaren.info/2007/02/invalid-xml-characters-when-valid-utf8_5873.html

        if (in == null){ 
            return null;
        }
        
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.
        
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) ||
                (current == 0xA) ||
                (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF))){
                out.append(current);
            }
        }
        return out.toString();
    } 
}
