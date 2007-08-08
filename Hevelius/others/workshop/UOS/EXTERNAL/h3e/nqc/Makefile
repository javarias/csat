#
# Build the RCX mount control command-line.
#
RCXLIB_DIR       = rcxlib
INCLUDE_DIR      = ../include
BIN_DIR          = .
CONFIG_DIR       = ../config

CPP_OPTS         =-I$(RCXLIB_DIR) -I$(INCLUDE_DIR) -lstdc++ -Wno-deprecated

all: 
	nqc -d -pgm 4 LegoMountProg4.rcx

firmware:
	nqc -firmware $(CONFIG_DIR)/firm0328.lgo

clean:
#	rm $(BIN_DIR)/LegoMountProg*.rcx

$(BIN_DIR)/LegoMountProg1.rcx: LegoMountProg1.nqc $(BIN_DIR)
	nqc -TRCX2 -I$(INCLUDE_DIR) -O$(BIN_DIR)/LegoMountProg1.rcx LegoMountProg1.nqc
	nqc -d -pgm 1 $(BIN_DIR)/LegoMountProg1.rcx
$(BIN_DIR)/LegoMountProg2.rcx: LegoMountProg2.nqc $(BIN_DIR)
	nqc -TRCX2 -I$(INCLUDE_DIR) -O$(BIN_DIR)/LegoMountProg2.rcx LegoMountProg2.nqc
	nqc -d -pgm 2 $(BIN_DIR)/LegoMountProg2.rcx
$(BIN_DIR)/LegoMountProg3.rcx: LegoMountProg3.nqc $(BIN_DIR)
	nqc -TRCX2 -I$(INCLUDE_DIR) -O$(BIN_DIR)/LegoMountProg3.rcx LegoMountProg3.nqc
	nqc -d -pgm 3 $(BIN_DIR)/LegoMountProg3.rcx
$(BIN_DIR)/LegoMountProg4.rcx: LegoMountProg4.nqc $(BIN_DIR)
	nqc -d -pgm 4 $(BIN_DIR)/LegoMountProg4.rcx
#$(BIN_DIR)/LegoMountProg5.rcx: LegoMountProg5.nqc $(BIN_DIR)
#	nqc -TRCX2 -I$(INCLUDE_DIR) -O$(BIN_DIR)/LegoMountProg5.rcx LegoMountProg5.nqc

support: $(BIN_DIR)/LegoMountProg1.rcx $(BIN_DIR)/LegoMountProg2.rcx $(BIN_DIR)/LegoMountProg3.rcx

# ___oOo___

