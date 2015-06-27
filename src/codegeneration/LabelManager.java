package codegeneration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kehanyang on 15/6/28.
 */
public class LabelManager {

    private static int countLabel;

    private static int countRealData;

    private static final String labelHeader = "_LABEL_";

    private static final String codeLabelHeader = "_CODE_LABEL_";

    private static final String realDataLabelHeader = "_REAL_DATA_";

    private static Map<Double, String> realConstPool = new HashMap<>();   // 实数常量池

    public static String createLabel() {
        return labelHeader + countLabel++;
    }

    public static String getLabel(int number) {
        return labelHeader + number;
    }

    public static String buildCodeLabel(int number) {
        return buildCodeLabel(String.valueOf(number));
    }

    public static String buildCodeLabel(String label) {
        return codeLabelHeader + label;
    }

    public static String getDataLabel(double realData, CodeGenerator codeGenerator) {
        String label = realConstPool.get(realData);
        if (label == null) {
            label = createDataLabel(realData, codeGenerator);
            realConstPool.put(realData, label);
        }
        return label;
    }

    private static String createDataLabel(double realData, CodeGenerator codeGenerator) {
        String label = realDataLabelHeader + countRealData++;
        codeGenerator.writeDataLine(label + " dd " + realData);
        return label;
    }
}
