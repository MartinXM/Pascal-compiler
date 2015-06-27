#ifndef _CGGLOBAL_H_
#define _CGGLOBAL_H_

#include <string>
#include <vector>
#include <map>
#include <sstream>

void cgerror(const char *message) {
	printf("%s\n", message);
	exit(1);
}

#define YYPARSER

#include "../global.h"
#include "CodeGenerator.h"
#include "LabelManager.h"
#include "CGFunction.h"
#include "Utility.h"
#include "CGExpId.h"
#include "CGExpConst.h"

#endif