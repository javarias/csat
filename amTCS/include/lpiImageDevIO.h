#ifndef _LPI_IMAGE_DEVIO_H_
#define _LPI_IMAGE_DEVIO_H_

#define CLEAR(x) memset (&(x), 0, sizeof (x))
#define CLAMP(x)  ((x)<0?0:((x)>255)?255:(x))

#include <baciDevIO.h>
#include <acstime.h>

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

class lpiImageDevIO: public DevIO<ACS::ROlongSeq_ptr>
{

	public:

	lpiImageDevIO(char *deviceName);
	virtual ~lpiImageDevIO();

	virtual bool initializeValue();

	ACS::ROlongSeq_ptr read(ACS::Time &timestamp)
	              throw (ACSErr::ACSbaseExImpl);

	void write(const ACS::ROlongSeq_ptr &value, ACS::Time &timestap)
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
