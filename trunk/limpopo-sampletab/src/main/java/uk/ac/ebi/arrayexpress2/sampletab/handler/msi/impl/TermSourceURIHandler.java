package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@ServiceProvider
public class TermSourceURIHandler extends MSIReadHandler {
    @Override
    protected void readValue(MSI msi, String value, int lineNumber, String... types) throws ParseException {
    	msi.termSourceURI.add(value);
    }

    public boolean canReadHeader(String header) {
        return header.equals("termsourceuri");
    }
}
