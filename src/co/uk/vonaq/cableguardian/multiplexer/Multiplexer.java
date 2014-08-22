package co.uk.vonaq.cableguardian.multiplexer;

import com.google.inject.ImplementedBy;
import co.uk.vonaq.cableguardian.exceptions.BoardDataException;
import java.io.IOException;

@ImplementedBy(DefaultMultiplexer.class)
public interface Multiplexer {

    public void setCable(int number) throws IOException;

    public int getLenth() throws IOException, BoardDataException;

}
