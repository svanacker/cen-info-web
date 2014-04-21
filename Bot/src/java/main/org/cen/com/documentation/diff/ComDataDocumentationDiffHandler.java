package org.cen.com.documentation.diff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Do the difference between two descriptors (one from the PC (programmed in
 * Java), the other one from the MAIN_BOARD (programmed in C)
 * 
 * @author Stephane Vanacker
 */
public class ComDataDocumentationDiffHandler {

    private static final int INDEX_HEADER = 2;

    /**
     * Compares the data descriptors and returns the differences.
     * 
     * @param descriptors1
     *            first list of descriptors
     * @param descriptors2
     *            second list of descriptors
     * @return the list of differences
     */
    public List<String> compareDescriptors(List<String> descriptors1, List<String> descriptors2) {
        ArrayList<String> differences = new ArrayList<String>();
        Collections.sort(descriptors1);
        Collections.sort(descriptors2);
        int n1 = descriptors1.size();
        int n2 = descriptors2.size();

        if (n1 != n2) {
            differences.add("Both descriptors have not the same size: source.length=" + n1 + ", destination.length="
                    + n2);
        }

        int i1 = 0, i2 = 0;
        while (i1 < n1 || i2 < n2) {
            String s1 = getDescriptor(descriptors1, i1);
            String s2 = getDescriptor(descriptors2, i2);
            if (s1 == null) {
                differences.add("missing in source: " + s2);
                i2++;
                continue;
            }
            if (s2 == null) {
                differences.add("missing in destination: " + s1);
                i1++;
                continue;
            }
            int n = s1.compareTo(s2);
            if (n == 0) {
                // strings match
                i1++;
                i2++;
            } else {
                if (isSameMethod(s1, s2)) {
                    // strings differ but are descriptors of the same method
                    differences.add("difference between source: " + s1 + ", and destination: " + s2);
                    i1++;
                    i2++;
                } else if (n < 0) {
                    differences.add("missing in destination: " + s1);
                    i1++;
                } else {
                    differences.add("missing in source: " + s2);
                    i2++;
                }
            }
        }

        return differences;
    }

    private String getDescriptor(List<String> descriptors1, int index) {
        if (descriptors1.size() <= index) {
            return null;
        } else {
            return descriptors1.get(index);
        }
    }

    private boolean isSameMethod(String s1, String s2) {
        String[] l1 = s1.split("\\|");
        String[] l2 = s2.split("\\|");
        if (l1.length >= INDEX_HEADER && l2.length >= INDEX_HEADER) {
            for (int i = 0; i <= INDEX_HEADER; i++) {
                if (!l1[i].equals(l2[i])) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
