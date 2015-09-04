package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import uk.ac.ebi.arrayexpress2.magetab.handler.Handler;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.handler.listener.HandlerEvent;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.Database;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@Handler
public class DatabaseURIHandler extends MSIReadHandler {
    @Override
    public void read(String header, String[] data, MSI msi, int lineNumber, int columnNumber) throws ParseException {
        fireHandlingStartedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
        //ensure it is a list at this point so that it is ordered
        //will always be if created by parser, otherwise not 
        
        for (int i = 0; i < data.length; i++){
            while (i >= msi.databases.size()){
                msi.databases.add(new Database(null, null, null));
            }
            String dbname = msi.databases.get(i).getName();
            String dbid = msi.databases.get(i).getID();
            msi.databases.set(i, new Database(dbname, data[i], dbid));
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
        return header.equals("databaseuri");
    }
}
