#ifndef _LPI_FRAME_DEVIO_H_
#define _LPI_FRAME_DEVIO_H_

#include <baciDevIO.h>
#include <acstime.h>

#define CLEAR(x) memset (&(x), 0, sizeof (x))
#define CLAMP(x)  ((x)<0?0:((x)>255)?255:(x))

typedef struct
{
  int is_abs;
  int len;
  int val;
  int unk;
} code_table_t;


/* local storage */

class lpiFrameDevIO: public DevIO<ACS::longSeq>
{

	public:

	lpiFrameDevIO(char *deviceName) throw (CORBA::SystemException);
	lpiFrameDevIO(void *data) throw (CORBA::SystemException);
	virtual ~lpiFrameDevIO() throw (CORBA::SystemException);

	virtual bool initializeValue() throw (CORBA::SystemException);

	ACS::longSeq read(ACS::Time &timestamp)
	      throw (ACSErr::ACSbaseExImpl);

	void write(const ACS::longSeq &value, ACS::Time &timestap)
	              throw (ACSErr::ACSbaseExImpl);

	private:
	int fd;
	int sonix_unknown;
	int init_done;
	unsigned int framesize;
	code_table_t table[256];

	void sonix_decompress_init() throw (CORBA::SystemException);
	int sonix_decompress (int width, int height, unsigned char *inp, unsigned char *outp) throw (CORBA::SystemException);
	void bayer2rgb24 (unsigned char *dst, unsigned char *src, long int WIDTH, long int HEIGHT) throw (CORBA::SystemException);
};

#endif /* _LPI_IMAGE_DEVIO_H_ */
