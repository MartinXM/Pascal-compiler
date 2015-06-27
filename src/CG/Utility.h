#ifndef _UTILITY_H_
#define _UTILITY_H_

#include "CGGlobal.h"
using namespace std;

class FileWriter {

private:
	FILE *file;
	FileWriter() {}

public:
	FileWriter(string fileName) {
		file = fopen(fileName.c_str(), "w");
	}
	FileWriter(FILE *file) : file(file) {}
	void write(string str) {
		fprintf(file, "%s", str.c_str());
	}
	void writeln(string str) {
		fprintf(file, "%s\n", str.c_str());
	}
	~FileWriter() {
		fclose(file);
	}
};

#endif