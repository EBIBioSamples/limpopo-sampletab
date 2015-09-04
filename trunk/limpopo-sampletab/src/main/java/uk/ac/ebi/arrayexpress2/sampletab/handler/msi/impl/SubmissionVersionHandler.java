package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import uk.ac.ebi.arrayexpress2.magetab.handler.Handler;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@Handler
public class SubmissionVersionHandler extends MSIReadHandler {
    @Override
    public int getAllowedLength() {
        return 1;
    }

    @Override
    protected void readValue(MSI msi, String value, int lineNumber, String... types)  {
    	msi.submissionVersion = value;
    }

    public boolean canReadHeader(String header) {
        return header.equals("submissionversion");
    }
}
