package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@ServiceProvider
public class OrganizationEmailHandler extends MSIReadHandler {
    @Override
    protected void readValue(MSI msi, String value, int lineNumber, String... types)  {
    	msi.organizationEmail.add(value);
    }

    public boolean canReadHeader(String header) {
        return header.equals("organizationemail");
    }
}
