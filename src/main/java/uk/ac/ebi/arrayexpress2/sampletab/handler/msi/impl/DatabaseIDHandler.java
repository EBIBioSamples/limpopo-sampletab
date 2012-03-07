package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.handler.listener.HandlerEvent;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@ServiceProvider
public class DatabaseIDHandler extends MSIReadHandler {
    @Override
    public void read(String header, String[] data, MSI msi, int lineNumber, int columnNumber) throws ParseException {
    	//TODO finish
        fireHandlingStartedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
        fireHandlingSucceededEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
        //fireHandlingFailedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
    }

    public boolean canReadHeader(String header) {
        return header.equals("databaseid");
    }

	@Override
	protected void readValue(MSI msi, String value, int lineNumber,
			String... types) throws ParseException {
		//not used
		getLog().error("Attempt to call unused method readValue()");
		throw new ParseException("Call to unused method readValue");
	}
}
