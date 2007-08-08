/*
 * "@(#) $Id: hptWebCamLocalProperties.h,v 1.1.1.1 2007/07/09 12:47:39 wg8 Exp $"
 *
 * $Log
 *
*/
webcamVideoDevice *vidDev;
webcamMinWidthIO *minWidthIO_p;
webcamMaxWidthIO *maxWidthIO_p;
webcamActWidthIO *actWidthIO_p;
webcamMinHeightIO *minHeightIO_p;
webcamMaxHeightIO *maxHeightIO_p;
webcamActHeightIO *actHeightIO_p;
webcamPaletteIO *paletteIO_p;
webcamColourIO *colourIO_p;
webcamContrastIO *contrastIO_p;
webcamBrightnessIO *brightnessIO_p;
webcamGammaIO *gammaIO_p;
webcamCompressionIO *compressionIO_p;
webcamFrameIO *frameIO_p;
webcamFPSIO *fpsIO_p;
webcamAGCIO *agcIO_p;
webcamAWBIO *awbIO_p;

bool DO_running,use_network_compression;
