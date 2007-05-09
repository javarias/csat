#include <stdio.h>
#include "telescope.h"

telescope_t *nexstar;
int main() {
	telescope_t nexstar_telescope = {
		.message = "",
		.version.mayor = 4,
		.version.minor = 1,
		.alignmentStatus = ALIGNED,
		
	};
	nexstar = &nexstar_telescope;

	echo("p");
	return 0;
}
