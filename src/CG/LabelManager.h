#ifndef _LABELMANAGER_H_
#define _LABELMANAGER_H_

#include "CGGlobal.h"
using namespace std;

class LabelManager {
    
private:
    static int count;
    static const string labelHeader;
    static const string codeLabelHeader;
    
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
};

int LabelManager::count = 0;
const string LabelManager::labelHeader = "_LABEL_";
const string LabelManager::codeLabelHeader = "_CODE_LABEL_";

#endif