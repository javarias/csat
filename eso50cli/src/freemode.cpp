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
				
	screen = SDL_SetVideoMode(320, 240, 32, 0);

	if (TTF_Init() == -1) {
	printf("Fallo al inicializar SDL_TTF");
	exit(-1);
	}
	
	TTF_Font* fuente;
	
	fuente = TTF_OpenFont("/var/lib/defoma/gs.d/dirs/fonts/Vera.ttf", 14);
	if(fuente == NULL) {
	printf("Fallo al abrir la fuente");
	exit(-1);
	}

	TTF_SetFontStyle(fuente, TTF_STYLE_BOLD | TTF_STYLE_ITALIC);
	
	SDL_Rect rect;
	rect = (SDL_Rect) {10, 10, 400, 400};

	SDL_Color color;
	SDL_Surface *txt_img[10];
	color = (SDL_Color) {255,100,100,255};
	
	txt_img[0] = TTF_RenderText_Blended(fuente,"FREE MODE" , color);
	if(txt_img[0] == NULL) {
	printf("Fallo al renderizar el texto");
	exit(-1);
	}
	txt_img[1] = TTF_RenderText_Blended(fuente,"Controls" , color);
	if(txt_img[1] == NULL) {
	printf("Fallo al renderizar el texto");
	exit(-1);
	}
	txt_img[2] = TTF_RenderText_Blended(fuente,"Arrow Up" , color);
	if(txt_img[2] == NULL) {
	printf("Fallo al renderizar el texto");
	exit(-1);
	}
	txt_img[3] = TTF_RenderText_Blended(fuente,"Arrow Down" , color);
	if(txt_img[3] == NULL) {
	printf("Fallo al renderizar el texto");
	exit(-1);
	}
	txt_img[4] = TTF_RenderText_Blended(fuente,"Arrow Right" , color);
	if(txt_img[4] == NULL) {
	printf("Fallo al renderizar el texto");
	exit(-1);
	}
	txt_img[5] = TTF_RenderText_Blended(fuente,"Arrow Left" , color);
	if(txt_img[5] == NULL) {
	printf("Fallo al renderizar el texto");
	exit(-1);
	}
	txt_img[6] = TTF_RenderText_Blended(fuente,"Velocity set" , color);
	if(txt_img[6] == NULL) {
	printf("Fallo al renderizar el texto");
	exit(-1);
	}
	txt_img[7] = TTF_RenderText_Blended(fuente,"Buttons [ f1 , f12 ]" , color);
	if(txt_img[7] == NULL) {
	printf("Fallo al renderizar el texto");
	exit(-1);
	}
	
	rect = (SDL_Rect) { 114, 10, 100, 30 };
	SDL_BlitSurface(txt_img[0], NULL, screen, &rect);

	rect = (SDL_Rect) { 10, 60, 100, 30 };
	SDL_BlitSurface(txt_img[1], NULL, screen, &rect);
	
	rect = (SDL_Rect) { 40, 75, 100, 30 };
	SDL_BlitSurface(txt_img[2], NULL, screen, &rect);

	rect = (SDL_Rect) { 40, 90, 100, 30 };
	SDL_BlitSurface(txt_img[3], NULL, screen, &rect);
	
	rect = (SDL_Rect) { 40, 105, 100, 30 };
	SDL_BlitSurface(txt_img[5], NULL, screen, &rect);

	rect = (SDL_Rect) { 40, 120, 100, 30 };
	SDL_BlitSurface(txt_img[5], NULL, screen, &rect);

	rect = (SDL_Rect) { 10, 150, 100, 30 };
	SDL_BlitSurface(txt_img[6], NULL, screen, &rect);

	rect = (SDL_Rect) { 40, 170, 100, 30 };
	SDL_BlitSurface(txt_img[7], NULL, screen, &rect);

	SDL_Flip(screen);

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
						if(event.key.type == SDL_KEYUP) 
							wref=130;
						}
					if(!strcmp("f2",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=120;
						}
					if(!strcmp("f3",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=110;
						}
					if(!strcmp("f4",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=100;
						}
					if(!strcmp("f5",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=90;
						}
					if(!strcmp("f6",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=80;
						}
					if(!strcmp("f7",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=70;
						}
					if(!strcmp("f8",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=60;
						}
					if(!strcmp("f9",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=50;
						}
					if(!strcmp("f10",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=40;
						}
					if(!strcmp("f11",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=30;
						}
					if(!strcmp("f12",SDL_GetKeyName(event.key.keysym.sym))){
						if(event.key.type == SDL_KEYUP) 
							wref=20;
						}
					break;
				case SDL_QUIT:		//The user has closed the SDL window
					running=0;
					break;
				}	
		}
	}
	TTF_CloseFont(fuente);
	TTF_Quit();
	SDL_Quit();
}