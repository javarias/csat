#include <SerialRS232.h>

using namespace std;

#define ALT_POS   0x1
#define ALT_NEG   0x2
#define AZM_POS   0x4
#define AZM_NEG   0x8

class Communication{

	private:
	SerialRS232 *sp;

	public:
	Communication(char *deviceName);
	~Communication();
	char *echo(char c);
	bool alignmentComplete();
	bool Slew(int rate, int direction);
};
