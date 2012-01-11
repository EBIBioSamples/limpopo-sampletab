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
    protected void readValue(MSI msi, String value, int lineNumber, String... types) throws ParseException {
    	try {
    		//TODO check synchronization of this
			msi.submissionUpdateDate = simpledateformat.parse(value);
		} catch (java.text.ParseException e) {
			//re-throw as an ae2 exception rather than java exception.
			throw new ParseException();
		}
    }

    public boolean canReadHeader(String header) {
        return (header.equals("submissionupdatedate") || header.equals("submissionmodificationdate"));
    }
}
