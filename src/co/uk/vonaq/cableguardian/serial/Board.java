package co.uk.vonaq.cableguardian.serial;

import com.google.inject.ImplementedBy;
import co.uk.vonaq.cableguardian.exceptions.BoardDataException;
import java.io.IOException;

@ImplementedBy(KT_96B.class)
public interface Board {

    public int getLenth() throws IOException, BoardDataException;

    public void sendreadRequest() throws IOException;
}
