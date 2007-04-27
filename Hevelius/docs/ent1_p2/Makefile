TARGET = entregable_parte2
#  los .tex estan todos en .; 
#  todas las imagenes en ./img; 
TEXS := $(wildcard *.tex)
IMGS := $(wildcard *.jpg)
FIGEPS := $(wildcard *.eps)
IMGEPS := $(addsuffix .eps, $(basename $(IMGS)))

DATE = `date '+%Y%m%d-%H%M'`

all:  $(TARGET).pdf

# Convert JPEG to EPS
%.eps: %.jpg
	@echo ***Building EPS from $<
	@convert $< $@

$(TARGET).dvi: $(addsuffix .eps, $(basename $(IMGS)))
	@echo ***Building DVI
	@latex $(TARGET)
	@while ( grep "Rerun to get cross-references" $(TARGET).log > /dev/null ); do \
		echo '***Re-running LaTeX to sort cross-references';     \
		latex $(TARGET);                                         \
	done

$(TARGET).pdf: $(TARGET).dvi
	@echo ***Building PDF
	@dvipdf $<
	@rm *.log
	@rm *.aux
	@rm *.dvi

clean:
	@echo ***Cleaning...
	@rm -f *.aux *.log *.tex~ *.bak
	@rm -f $(TARGET).pdf $(TARGET).dvi
	@rm -f img/*~ img/*.bak img/*.eps
	@echo ***Done cleaning
