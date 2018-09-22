<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/js/highslide.css"/>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highslide-with-gallery.js"></script>
<script type="text/javascript">
	hs.graphicsDir = '<%=controller.getStaticPath(request)%>/js/graphics/';
	hs.align = 'center';
	hs.transitions = ['expand', 'crossfade'];
	hs.wrapperClassName = 'dark borderless floating-caption';
	hs.fadeInOut = true;
	hs.dimmingOpacity = .75;
	hs.numberPosition = 'caption';
	hs.lang.number = "%1/%2";

	// Add the controlbar
	if (hs.addSlideshow) hs.addSlideshow({
    slideshowGroup: 'gallery',
    interval: 5000,
    repeat: false,
    useControls: true,
    fixedControls: 'fit',
    overlayOptions: {
        opacity: .6,
        position: 'bottom center',
        hideOnMouseOut: true,
        offsetY: 100
    },
	thumbstrip: {
		position: 'bottom center',
		mode: 'horizontal',
		relativeTo: 'viewport'
	}
}); 
</script>