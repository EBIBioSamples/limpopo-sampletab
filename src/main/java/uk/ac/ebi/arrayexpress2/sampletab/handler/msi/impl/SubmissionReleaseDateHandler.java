package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.arrayexpress2.magetab.handler.Handler;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@Handler
public class SubmissionReleaseDateHandler extends MSIReadHandler {
    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public int getAllowedLength() {
        return 1;
    }

    @Override
    protected void readValue(MSI msi, String value, int lineNumber, String... types) throws ParseException  {
		String[] date_formats = { "yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy",
				"dd-MM-yyyy", "yyyy MMM dd", "yyyy dd MMM", "dd MMM yyyy",
				"yyyyMMdd", "ddMMyyyy", "yyyy\\MM\\dd", "dd\\MM\\yyyy" };
		
		for (String formatdate : date_formats) {
			try {
				msi.submissionReleaseDate = new SimpleDateFormat(
                        formatdate).parse(value);
                log.trace("Submission Release Date format is " + formatdate);
                break;
            } catch (java.text.ParseException e) {
                log.trace("Submission Release Date format is not " + formatdate);
                continue;
            }
		}

	}    

    public boolean canReadHeader(String header) {
        return header.equals("submissionreleasedate");
    }
}
