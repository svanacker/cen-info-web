<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:subview id="visionSubview">
		<h:panelGrid id="visionViewPanel" rendered="#{VisionView.available}">
			<ui:include src="vision/videoControl.jsp" />
			<ui:include src="vision/videoPreview.jsp" />
<!--			<ui:include src="vision/targetHandler.jsp" />-->
		</h:panelGrid>
		<h:outputText value="Not available"
			rendered="#{not VisionView.available}" />
	</f:subview>

</ui:composition>
