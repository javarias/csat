void freemode(Communication *com){
	SDL_Surface *screen;
	SDL_Event event;
	int running=1;
	float wref=20;
	if (SDL_Init(SDL_INIT_VIDEO) != 0) {
		printf("Unable to initialize SDL: %s\n", SDL_GetError());
		return 1;
	}

	atexit(SDL_Quit);
				
	screen = SDL_SetVideoMode(200, 100, 32, 0);

	while(running) {
		while(SDL_PollEvent(&event)) {
			switch(event.type){
				case SDL_KEYDOWN:	//A key has been pressed
				case SDL_KEYUP:		//A key has been released
					if(!strcmp("up",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYDOWN) 
						com->writeTo(wref,162,1,1,1,1);
						else com->writeTo(wref,162,1,0,1,1);
						}
					if(!strcmp("down",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYDOWN) 
						com->writeTo(wref,162,1,1,0,1);
						else com->writeTo(wref,162,1,0,0,1);
						}
					if(!strcmp("right",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYDOWN) 
						com->writeTo(wref,164,1,1,1,1);
						else com->writeTo(wref,164,1,0,1,1);
						}
					if(!strcmp("left",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYDOWN) 
						com->writeTo(wref,164,1,1,0,1);
						else com->writeTo(wref,164,1,0,0,1);
						}
					if(!strcmp("f1",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) {
							printf("Ingrese la nueva velocidad: ");
							scanf("%f",&wref);
							}
						}	
					break;
				case SDL_QUIT:		//The user has closed the SDL window
					running=0;
					break;
				}	
		}
	}
	SDL_Quit();
}