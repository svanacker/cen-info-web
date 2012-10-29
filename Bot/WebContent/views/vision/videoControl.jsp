<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>

		<rich:panel id="videoControlPanel">
			<f:facet name="header">
				<h:outputText value="Video control" />
			</f:facet>
			<h:form rendered="#{VideoControlView.available}">
				<h:selectBooleanCheckbox id="automaticGain"
					value="#{VideoControlView.automaticGain}">
					<a4j:support event="onchange" reRender="videoControlPanel" />
				</h:selectBooleanCheckbox>
				<h:outputLabel for="automaticGain" value="Automatic gain" />
				<h:panelGrid columns="2">
					<h:outputLabel for="gainSlider" value="Gain" />
					<rich:inputNumberSlider id="gainSlider"
						value="#{VideoControlView.gain}" minValue="0" maxValue="100"
						step="1" disabled="#{VideoControlView.automaticGain}">
						<a4j:support event="onchange" reRender="videoControlPanel" />
					</rich:inputNumberSlider>
					<h:outputLabel for="exposureSlider" value="Exposure" />
					<rich:inputNumberSlider id="exposureSlider"
						value="#{VideoControlView.exposure}" minValue="0" maxValue="100"
						step="1" disabled="#{VideoControlView.automaticGain}">
						<a4j:support event="onchange" reRender="videoControlPanel" />
					</rich:inputNumberSlider>
				</h:panelGrid>
			</h:form>
			<h:outputText value="Not available"
				rendered="#{not VideoControlView.available}" />
		</rich:panel>

	</f:view>

</ui:composition>
