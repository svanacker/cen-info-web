package org.cen.com;

import java.util.HashSet;
import java.util.Set;

import org.cen.com.in.InData;
import org.cen.com.in.UntypedInData;

/**
 * The service which manage all Decoders.
 */
public class InDataDecodingService {

    /**
     * All decoders which are registered by the system.
     */
    private Set<InDataDecoder> decoders = new HashSet<InDataDecoder>();

    /**
     * Build the decoder corresponding to the system.
     * 
     * @param data the data from the serial interface
     * @return an object encapsulating the message.
     * @throws IllegalComDataException
     */
    public InData decode(String data) throws IllegalComDataException {
        String header = data.substring(0, 1);
        InDataDecoder decoder = getDecoder(header);
        if (decoder != null) {
            return decoder.decode(data);
        } else {
            return new UntypedInData(data);
        }
    }

    /**
     * Return the right decoder which must be used to handle the message with the header given in paramters.
     * 
     * @param header the header for which we must determine the right decoder to handle in data
     * @return
     */
    public InDataDecoder getDecoder(String header) {
        for (InDataDecoder inDataDecoder : decoders) {
            Set<String> handledHeaders = inDataDecoder.getHandledHeaders();
            if (handledHeaders.contains(header)) {
                return inDataDecoder;
            }
        }
        return null;
    }

    /**
     * Returns the registered decoders.
     * 
     * @return the registered decoders
     */
    public Set<InDataDecoder> getDecoders() {
        return decoders;
    }

    /**
     * Register a decoder which can handle many message.
     * 
     * @param decoder the decoder that we want to register in the system.
     */
    public void registerDecoder(InDataDecoder decoder) {
        decoders.add(decoder);
    }
}
