package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import java.util.List;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.handler.listener.HandlerEvent;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Database;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@ServiceProvider
public class DatabaseIDHandler extends MSIReadHandler {
    @Override
    public void read(String header, String[] data, MSI msi, int lineNumber, int columnNumber) throws ParseException {
        fireHandlingStartedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
        //ensure it is a list at this point so that it is ordered
        //will always be if created by parser, otherwise not 
        List<Database> dbs = (List<Database>) msi.databases;
        
        for (int i = 0; i < data.length; i++){
            while (i >= dbs.size()){
                dbs.add(new Database(null, null, null));
            }
            String dbname = dbs.get(i).getName();
            String dburi = dbs.get(i).getURI();
            dbs.set(i, new Database(dbname, dburi, data[i]));
        }
        
        fireHandlingSucceededEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
        //fireHandlingFailedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
    }

	@Override
	protected void readValue(MSI msi, String value, int lineNumber,
			String... types) throws ParseException {
		//not used
		getLog().error("Attempt to call unused method readValue()");
		throw new ParseException("Call to unused method readValue");
	}

    public boolean canReadHeader(String header) {
        return header.equals("databaseid");
    }
}
