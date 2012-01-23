package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import java.text.SimpleDateFormat;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@ServiceProvider
public class SubmissionReleaseDateHandler extends MSIReadHandler {
	private SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd");
    @Override
    public int getAllowedLength() {
        return 1;
    }

    @Override
    protected void readValue(MSI msi, String value, int lineNumber, String... types)  {
    	try {
    		//TODO check synchronization of this
			msi.submissionReleaseDate = simpledateformat.parse(value);
		} catch (java.text.ParseException e) {
            //TODO report this better
		    //do nothing
		}
    }

    public boolean canReadHeader(String header) {
        return header.equals("submissionreleasedate");
    }
}
