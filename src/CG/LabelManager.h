#ifndef _LABELMANAGER_H_
#define _LABELMANAGER_H_

#include "CGGlobal.h"
using namespace std;

class LabelManager {
    
private:
    static int countLabel;
    static int countRealData;
    static const string labelHeader;
    static const string codeLabelHeader;
    static const string realDataLabelHeader;
    static map<double, string> realConstPool;   // 实数常量池
    
public:
    static string createLabel() {
    	stringstream ss;
    	ss << labelHeader << count++;
    	return ss.str();
    }
    static string getLabel(int number) {
    	stringstream ss;
    	ss << labelHeader << number;
    	return ss.str();
    }
    static string buildCodeLabel(int number) {
    	stringstream ss;
    	ss << codeLabelHeader << number;
    	return ss.str();
    }
    static string buildCodeLabel(string label) {
    	stringstream ss;
    	ss << codeLabelHeader << label;
    	return ss.str();
    }
    static string getDataLabel(double realData, CodeGenerator *cg) {
        if (realConstPool.count(realData) == 0) {
            string label = createDataLabel(realData, cg);
            realConstPool[realData] = label;
            return label;
        }
        else {
            return realConstPool[realData];
        }
    }

private:
    static string createDataLabel(double realData, CodeGenerator *cg) {
        stringstream ss;
        ss << realDataLabelHeader << countRealData;
        string label = ss.str();
        ss << " dd " << realData;
        cg.writelnData(ss.str());
        return label;
    }
};

int LabelManager::countLabel = 0;
int LabelManager::countRealData = 0;
const string LabelManager::labelHeader = "_LABEL_";
const string LabelManager::codeLabelHeader = "_CODE_LABEL_";
const string LabelManager::realDataLabelHeader = "_REAL_DATA_";

#endif