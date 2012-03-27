package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@ServiceProvider
public class SubmissionIdentifierHandler extends MSIReadHandler {
    @Override
    public int getAllowedLength() {
        return 1;
    }

    @Override
    protected void readValue(MSI msi, String value, int lineNumber, String... types)  {
    	msi.submissionIdentifier = value;
    }

    public boolean canReadHeader(String header) {
        return header.equals("submissionidentifier");
    }
}
