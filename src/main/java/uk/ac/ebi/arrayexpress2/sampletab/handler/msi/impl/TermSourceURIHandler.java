package uk.ac.ebi.arrayexpress2.sampletab.handler.msi.impl;

import java.util.List;

import net.sourceforge.fluxion.spi.ServiceProvider;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.handler.listener.HandlerEvent;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.MSI;
import uk.ac.ebi.arrayexpress2.sampletab.datamodel.msi.TermSource;
import uk.ac.ebi.arrayexpress2.sampletab.handler.msi.MSIReadHandler;

@ServiceProvider
public class TermSourceURIHandler extends MSIReadHandler {
    @Override
    public void read(String header, String[] data, MSI msi, int lineNumber, int columnNumber) throws ParseException {
        fireHandlingStartedEvent(new HandlerEvent(this, HandlerEvent.Type.READ, data));
        //ensure it is a list at this point so that it is ordered
        //will always be if created by parser, otherwise not 
        List<TermSource> tss = (List<TermSource>) msi.termSources;
        
        for (int i = 0; i < data.length; i++){
            while (i >= tss.size()){
                tss.add(new TermSource(null, null, null));
            }
            String tsname = tss.get(i).getName();
            String tsv = tss.get(i).getVersion();
            tss.set(i, new TermSource(tsname, data[i], tsv));
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
        return header.equals("termsourceuri");
    }
}
