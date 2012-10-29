<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>
		<rich:panel>
			<rich:paint2D id="targetHandlerInput" width="320" height="240"
				format="jpg" paint="#{TargetHandlerView.paintInput}"
				data="#{TargetHandlerView.timeStamp}" border="0">
			</rich:paint2D>
			<rich:paint2D id="targetHandlerFilter" width="320" height="240"
				format="jpg" paint="#{TargetHandlerView.paintFilter}"
				data="#{TargetHandlerView.timeStamp}" border="0">
			</rich:paint2D>
		</rich:panel>
		<h:form>
			<rich:panel>
				<f:facet name="header">
					<h:outputText value="Color filter" />
				</f:facet>
				<h:panelGrid columns="2">
					<h:outputLabel for="colorAngleSlider" value="Angle color" />
					<rich:inputNumberSlider id="colorAngleSlider"
						value="#{TargetHandlerView.colorAngle}" minValue="0"
						maxValue="628" step="5" />
					<h:outputLabel for="intensityThresholdSlider"
						value="Intensity threshold" />
					<rich:inputNumberSlider id="intensityThresholdSlider"
						value="#{TargetHandlerView.intensityThreshold}" minValue="0"
						maxValue="100" step="5" />
					<h:outputLabel for="saturationThresholdSlider"
						value="Saturation threshold" />
					<rich:inputNumberSlider id="saturationThresholdSlider"
						value="#{TargetHandlerView.saturationThreshold}" minValue="0"
						maxValue="100" step="5" />
					<h:outputLabel for="slopeSlider"
						value="Slope (bandwidth)" />
					<rich:inputNumberSlider id="slopeSlider"
						value="#{TargetHandlerView.slope}" minValue="0"
						maxValue="100" step="5" />
				</h:panelGrid>
			</rich:panel>
			<rich:panel>
				<f:facet name="header">
					<h:outputText value="Target search" />
				</f:facet>
				<h:panelGrid columns="2">
					<h:outputLabel for="weightThresholdSlider" value="Weight threshold" />
					<rich:inputNumberSlider id="weightThresholdSlider"
						value="#{TargetHandlerView.weightThreshold}" minValue="0"
						maxValue="200" step="5" />
					<h:outputLabel for="topBorderSlider" value="Top border" />
					<rich:inputNumberSlider id="topBorderSlider"
						value="#{TargetHandlerView.topBorder}" minValue="0" maxValue="100"
						step="5" />
					<h:outputLabel for="bottomBorderSlider" value="Bottom border" />
					<rich:inputNumberSlider id="bottomBorderSlider"
						value="#{TargetHandlerView.bottomBorder}" minValue="0"
						maxValue="100" step="5" />
					<h:outputLabel for="oldDataHistorySlider"
						value="Multiplicative ratio for old data" />
					<rich:inputNumberSlider id="oldDataHistorySlider"
						value="#{TargetHandlerView.oldDataRatio}" minValue="0"
						maxValue="200" step="5" />
					<h:outputLabel for="newDataHistorySlider"
						value="Multiplicative ratio for new data" />
					<rich:inputNumberSlider id="newDataHistorySlider"
						value="#{TargetHandlerView.newDataRatio}" minValue="0"
						maxValue="200" step="5" />
				</h:panelGrid>
				<p /><a4j:commandButton action="#{TargetHandlerView.clearHistory}"
					value="Clear history buffer" />
			</rich:panel>
			<a4j:poll interval="1000"
				reRender="targetHandlerFilter,targetHandlerInput" />
		</h:form>
	</f:view>

</ui:composition>
