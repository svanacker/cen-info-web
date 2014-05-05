package org.cen.com.out;

public class RawOutData extends OutData {

    private final String data;

    public RawOutData(String data) {
        super();
        this.data = data;
    }

    @Override
    public String getHeader() {
        return data;
    }
}
