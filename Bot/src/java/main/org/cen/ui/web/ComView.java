package org.cen.ui.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.cen.com.ComDebugListener;
import org.cen.com.IComService;
import org.cen.com.documentation.ComDataDocumentationHandler;
import org.cen.com.documentation.diff.ComDataDocumentationDiffHandler;
import org.cen.com.in.InData;
import org.cen.com.in.InDataListener;
import org.cen.com.in.RawInData;
import org.cen.com.out.OutData;
import org.cen.com.out.OutDataListener;
import org.cen.com.out.RawOutData;
import org.cen.robot.IRobotService;
import org.cen.robot.IRobotServiceProvider;
import org.cen.robot.RobotUtils;
import org.cen.robot.match.MatchData;
import org.springframework.web.context.ServletContextAware;

/**
 * Presentation Object to store the data which are exchanged between the client
 * and the server.
 * 
 * @author svanacker
 * @version 24/02/2008
 */
public class ComView implements IRobotService, InDataListener, ComDebugListener, OutDataListener, ServletContextAware {

    private int currentStatement = 0;

    /**
     * The list of Indata received by the server.
     */
    protected List<InData> inDataList;

    private String input = "";

    private String inputFromMicrocontroller;

    private String dataSignatures;

    private final List<OutDataDescriptor> outDataDescriptors = new ArrayList<OutDataDescriptor>();

    /**
     * The list of Outdata sent to the client.
     */
    protected List<OutData> outDataList;

    /**
     * The list of Rawdata received by the server.
     */
    protected List<RawInData> rawInDataList;

    /**
     * Access to the service provider.
     */
    protected IRobotServiceProvider servicesProvider;

    private ServletContext servletContext;

    private boolean waitForAck = true;

    private String inputDataSignatures;

    private String dataSignaturesComparison;

    /**
     * Constructor.
     */
    public ComView() {
        inDataList = new ArrayList<InData>();
        rawInDataList = new ArrayList<RawInData>();
        outDataList = new ArrayList<OutData>();
        // Register
    }

    public void checkDifferences() {
        OutDataAnalyzer analyzer = new OutDataAnalyzer();
        Map<Class<OutData>, String> map = analyzer.findOutData(servletContext);
        List<Class<OutData>> list = analyzer.getSortedList(map);
        for (Class<OutData> data : list) {
            String cup = analyzer.getCup(data);
            String header = map.get(data);
            outDataDescriptors.add(new OutDataDescriptor(cup, data.getName(), header));
        }
    }

    /**
     * Clear the data of all List.
     */
    public void clear() {
        inDataList.clear();
        rawInDataList.clear();
        outDataList.clear();
    }

    public void compareSignatures() {
        // Reading input text
        List<String> inputMethods = new ArrayList<String>();
        String[] signatures = inputDataSignatures.split("\n");
        for (String signature : signatures) {
            inputMethods.add(signature);
        }

        // Comparison
        ComDataDocumentationHandler doc = new ComDataDocumentationHandler(servicesProvider);
        ComDataDocumentationDiffHandler diffHandler = new ComDataDocumentationDiffHandler();
        doc.setWarnIfUndocumented(false);
        List<String> methods = doc.getMethodDescriptors();
        List<String> results = diffHandler.compareDescriptors(methods, inputMethods);
        StringBuilder b = new StringBuilder();
        for (String s : results) {
            b.append(s);
            b.append("<br>");
        }
        if (b.length() == 0) {
            dataSignaturesComparison = "<i>Data match</i>";
        } else {
            dataSignaturesComparison = b.toString();
        }
    }

    public int getCurrentStatement() {
        return currentStatement;
    }

    public String getDataSignatures() {
        return dataSignatures;
    }

    public String getDataSignaturesComparison() {
        return dataSignaturesComparison;
    }

    public List<InData> getInDataList() {
        return inDataList;
    }

    public String getInput() {
        return input;
    }

    public String getInputFromMicrocontroller() {
        return inputFromMicrocontroller;
    }

    public String getInputDataSignatures() {
        return inputDataSignatures;
    }

    public List<OutDataDescriptor> getOutDataDescriptors() {
        return outDataDescriptors;
    }

    public List<OutData> getOutDataList() {
        return outDataList;
    }

    public List<RawInData> getRawInDataList() {
        return rawInDataList;
    }

    public OutData getStatement(int index) {
        MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);
        List<OutData> statements = (List<OutData>) data.get("statements");
        return statements.get(index);
    }

    public List<SelectItem> getStatements() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        MatchData data = RobotUtils.getRobotAttribute(MatchData.class, servicesProvider);

        List<OutData> statements = (List<OutData>) data.get("statements");
        int i = 0;
        if (statements != null) {
            for (OutData s : statements) {
                SelectItem item = new SelectItem(i++, s.getMessage());
                items.add(item);
            }
        }
        return items;
    }

    public boolean isWaitForAck() {
        return waitForAck;
    }

    @Override
    public void onInData(InData data) {
        inDataList.add(data);
    }

    @Override
    public void onOutDataEvent(OutData outData) {
        outDataList.add(outData);
    }

    @Override
    public void onRawInData(RawInData rawData) {
        rawInDataList.add(rawData);
    }

    public void populateDescriptors() {
        OutDataAnalyzer analyzer = new OutDataAnalyzer();
        Map<Class<OutData>, String> map = analyzer.findOutData(servletContext);
        List<Class<OutData>> list = analyzer.getSortedList(map);
        for (Class<OutData> data : list) {
            String cup = analyzer.getCup(data);
            String header = map.get(data);
            outDataDescriptors.add(new OutDataDescriptor(cup, data.getName(), header));
        }
    }

    public void populateSignatures() {
        ComDataDocumentationHandler doc = new ComDataDocumentationHandler(servicesProvider);
        doc.setWarnIfUndocumented(false);
        List<String> methods = doc.getMethodDescriptors();
        StringBuilder b = new StringBuilder();
        for (String method : methods) {
            b.append(method);
            b.append("<br>");
        }
        dataSignatures = b.toString();
    }

    public void reconnect() {
        IComService comService = servicesProvider.getService(IComService.class);
        comService.reconnect();
    }

    public void sendData() {
        IComService comService = servicesProvider.getService(IComService.class);
        OutData data = new RawOutData(input);
        data.setWaitForAck(waitForAck);
        comService.writeOutData(data);
        input = "";
    }

    public void simulateDataFromMicrocontroller() {
        IComService comService = servicesProvider.getService(IComService.class);
        comService.writeInData(inputFromMicrocontroller);
    }

    public void sendStatement() {
        if (currentStatement >= 0) {
            IComService comService = servicesProvider.getService(IComService.class);
            // We go to the following instruction when we send an instruction
            OutData data = getStatement(currentStatement++);
            data.setWaitForAck(false);
            comService.writeOutData(data);
        }
    }

    public void setCurrentStatement(int currentStatement) {
        this.currentStatement = currentStatement;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setInputFromMicrocontroller(String inputFromMicrocontroller) {
        this.inputFromMicrocontroller = inputFromMicrocontroller;
    }

    public void setInputDataSignatures(String inputDataSignatures) {
        this.inputDataSignatures = inputDataSignatures;
    }

    @Override
    public void setServicesProvider(IRobotServiceProvider servicesProvider) {
        this.servicesProvider = servicesProvider;
        IComService comService = servicesProvider.getService(IComService.class);
        comService.addInDataListener(this);
        comService.addDebugListener(this);
        comService.addOutDataListener(this);
    }

    @Override
    public void setServletContext(ServletContext context) {
        servletContext = context;
    }

    public void setWaitForAck(boolean waitForAck) {
        this.waitForAck = waitForAck;
    }

    public String String() {
        return getClass().getSimpleName() + "[inDataList:size=" + inDataList.size() + ", rawInDataList="
                + rawInDataList.size() + ", outDataList=" + outDataList.size() + "]";
    }
}
