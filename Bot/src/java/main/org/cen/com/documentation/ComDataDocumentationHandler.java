package org.cen.com.documentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cen.com.IComService;
import org.cen.com.InDataDecoder;
import org.cen.com.out.OutData;
import org.cen.com.out.OutDataSender;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.device.IRobotDevice;
import org.cen.robot.device.IRobotDevicesHandler;

/**
 * Handles the documentation of communication incoming and outgoing data. Builds
 * a list of strings representing methods of devices with arguments and their
 * lengths and types. @see ComDataDocumentationMethodSignatureBuilder and test
 * to see the syntax
 * 
 * @author Emmanuel ZURMELY
 */
public class ComDataDocumentationHandler {

    private final IRobotServiceProvider provider;

    private final ComDataDocumentationMethodSignatureBuilder builder;

    private boolean warnIfUndocumented = true;

    /**
     * Constructor.
     * 
     * @param provider
     *            the services provider
     */
    public ComDataDocumentationHandler(IRobotServiceProvider provider) {
        super();
        this.provider = provider;
        builder = new ComDataDocumentationMethodSignatureBuilder();
    }

    private Set<InDataDecoder> getDecoders() {
        IComService comService = provider.getService(IComService.class);
        return comService.getDecodingService().getDecoders();
    }

    private Set<Class<? extends OutData>> getEncoders() {
        HashSet<Class<? extends OutData>> results = new HashSet<Class<? extends OutData>>();
        IRobotDevicesHandler handler = provider.getService(IRobotDevicesHandler.class);
        Collection<IRobotDevice> devices = handler.getDevices().values();
        for (IRobotDevice device : devices) {
            Class<? extends IRobotDevice> deviceInterface = device.getClass();
            OutDataSender sender = deviceInterface.getAnnotation(OutDataSender.class);
            if (sender != null) {
                Class<? extends OutData>[] classes = sender.classes();
                for (Class<? extends OutData> outDataClass : classes) {
                    results.add(outDataClass);
                }
            }
        }
        return results;
    }

    /**
     * Returns the list of the methods descriptors.
     * 
     * @return the list of the methods descriptors
     */
    public List<String> getMethodDescriptors() {
        List<String> results = new ArrayList<String>();
        List<String> fromInDataDecoders = getSignatureFromInDataDecoders();
        List<String> fromOutData = getSignatureFromOutData();
        results.addAll(fromInDataDecoders);
        results.addAll(fromOutData);

        return results;
    }

    private List<String> getSignatureFromInDataDecoders() {
        List<String> results = new ArrayList<String>();
        Set<InDataDecoder> decoders = getDecoders();
        for (InDataDecoder decoder : decoders) {
            DeviceDataSignature signature = decoder.getClass().getAnnotation(DeviceDataSignature.class);
            if (signature == null) {
                // The decoder has no signature
                if (warnIfUndocumented) {
                    results.add("undocumented decoder: " + decoder.getClass().getName());
                }
            } else {
                builder.addMethods(results, signature);
            }
        }
        return results;
    }

    private List<String> getSignatureFromOutData() {
        List<String> results = new ArrayList<String>();
        Set<Class<? extends OutData>> encoders = getEncoders();
        for (Class<? extends OutData> encoder : encoders) {
            DeviceDataSignature signature = encoder.getAnnotation(DeviceDataSignature.class);
            if (signature == null) {
                // The encoder has no signature
                if (warnIfUndocumented) {
                    results.add("undocumented encoder: " + encoder.getClass().getName());
                }
            } else {
                builder.addMethods(results, signature);
            }
        }
        return results;
    }

    /**
     * Returns the flag that determines whether the undocumented data are listed
     * or not.
     * 
     * @return TRUE if undocumented data are listed, FALSE otherwise
     */
    public boolean getWarnIfUndocumented() {
        return warnIfUndocumented;
    }

    /**
     * Sets then flag that determines whether the undocumented data are listed
     * or not.
     * 
     * @param warnIfUndocumented
     *            TRUE to list undocumented data, FALSE otherwise
     */
    public void setWarnIfUndocumented(boolean warnIfUndocumented) {
        this.warnIfUndocumented = warnIfUndocumented;
    }
}
