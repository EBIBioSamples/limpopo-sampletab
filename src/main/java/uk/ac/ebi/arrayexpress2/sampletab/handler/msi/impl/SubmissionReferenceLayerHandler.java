package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@ServiceProvider
public class SubmissionReferenceLayerHandler extends MSIReadHandler {
    @Override
    public int getAllowedLength() {
        return 1;
    }

    @Override
    protected void readValue(MSI msi, String value, int lineNumber, String... types) throws ParseException {
    	msi.submissionReferenceLayer = (value.toLowerCase() == "true");
    }

    public boolean canReadHeader(String header) {
        return header.equals("submissionreferencelayer");
    }
}
