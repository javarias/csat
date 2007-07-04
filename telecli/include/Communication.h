#include <SerialRS232.h>

using namespace std;

/** Movement in the Altitud axis in positive direction */
#define ALT_POS   0x1

/** Movement in the Altitud axis in negative direction */
#define ALT_NEG   0x2

/** Movement in the Azimuth axis in positive direction */
#define AZM_POS   0x4

/** Movement in the Azimuth axis in negative direction */
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
