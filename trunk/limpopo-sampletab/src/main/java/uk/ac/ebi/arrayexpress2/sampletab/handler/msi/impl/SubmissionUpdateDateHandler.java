package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import java.text.SimpleDateFormat;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@ServiceProvider
public class SubmissionUpdateDateHandler extends MSIReadHandler {
	private SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd");
    @Override
    public int getAllowedLength() {
        return 1;
    }

    @Override
    protected void readValue(MSI msi, String value, int lineNumber, String... types) throws ParseException  {
        
        if (!value.matches("[0-9]{2,4}/[0-9]{1,2}/[0-9]{1,2}"))
            throw new ParseException("Invalid SubmissionUpdateDate \""+value+"\"");
        
    	try {
    		//TODO check synchronization of this
			msi.submissionUpdateDate = simpledateformat.parse(value);
		} catch (java.text.ParseException e) {
            throw new ParseException(e);
		}
    }

    public boolean canReadHeader(String header) {
        return (header.equals("submissionupdatedate") || header.equals("submissionmodificationdate"));
    }
}
