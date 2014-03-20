package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import java.text.SimpleDateFormat;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@ServiceProvider
public class SubmissionReleaseDateHandler extends MSIReadHandler {
    @Override
    public int getAllowedLength() {
        return 1;
    }

    @Override
    protected void readValue(MSI msi, String value, int lineNumber, String... types) throws ParseException  {
		boolean flag = false;
		String[] date_formats = { "yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy",
				"dd-MM-yyyy", "yyyy MMM dd", "yyyy dd MMM", "dd MMM yyyy",
				"yyyyMMdd", "ddMMyyyy", "yyyy\\MM\\dd", "dd\\MM\\yyyy" };
		for (String formatdate : date_formats) {
			try {
				synchronized (SubmissionUpdateDateHandler.simpledateformat) {
					msi.submissionReleaseDate = new SimpleDateFormat(formatdate)
							.parse(value);
					System.out.println("the date format is" + formatdate);
					flag = true;
					break;
				}
			} catch (Exception e) {
				System.out.println("The date format is not " + formatdate);
				continue;
			}
		}

	}
    /**   if (!value.matches("[0-9]{2,4}/[0-9]{1,2}/[0-9]{1,2}"))
            throw new ParseException("Invalid SubmissionReleaseDate \""+value+"\"");
        
    	try {
    	    synchronized (SubmissionUpdateDateHandler.simpledateformat){
                msi.submissionReleaseDate = SubmissionUpdateDateHandler.simpledateformat.parse(value);
    	    }
		} catch (java.text.ParseException e) {
            throw new ParseException(e);
		}
    }*/
    

    public boolean canReadHeader(String header) {
        return header.equals("submissionreleasedate");
    }
}
