#ifndef _LPI_IMAGE_DEVIO_H_
#define _LPI_IMAGE_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#define CLEAR(x) memset (&(x), 0, sizeof (x))
#define CLAMP(x)  ((x)<0?0:((x)>255)?255:(x))

struct buffer
{
	void * start;
	size_t length;
};

typedef struct
{
  int is_abs;
  int len;
  int val;
  int unk;
} code_table_t;


/* local storage */

class lpiImageDevIO: public DevIO<CORBA::LongSeq>
{

	public:

	lpiImageDevIO(char *deviceName);
	lpiImageDevIO(void *data);
	virtual ~lpiImageDevIO();

	virtual bool initializeValue();

	CORBA::LongSeq read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	void write(const CORBA::LongSeq &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	private:
	int fd;
	int sonix_unknown;
	static int init_done;
	static code_table_t table[256];
	struct buffer *buffers;

	void sonix_decompress_init();
	int sonix_decompress (int width, int height, unsigned char *inp, unsigned char *outp);
	void bayer2rgb24 (unsigned char *dst, unsigned char *src, long int WIDTH, long int HEIGHT);
};

#endif /* _LPI_IMAGE_DEVIO_H_ */
