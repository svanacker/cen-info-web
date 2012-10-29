<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>

		<rich:panel id="videoPreviewPanel">
			<f:facet name="header">
				<h:outputText value="Video preview" />
			</f:facet>
			<rich:panel>
				<h:form>
					<rich:paint2D id="videoPreviewInput" width="320" height="240"
						format="jpg" paint="#{VideoPreview.paintInput}"
						data="#{VideoPreview.timeStamp}" border="0">
					</rich:paint2D>
					<rich:paint2D id="videoPreviewFilter" width="320" height="240"
						format="jpg" paint="#{VideoPreview.paintFilter}"
						data="#{VideoPreview.timeStamp}" border="0">
					</rich:paint2D>
					<rich:inputNumberSlider id="gainSlider"
						value="#{VideoPreview.refreshInterval}" minValue="500"
						maxValue="5000" step="500">
						<a4j:support event="onchange" reRender="videoControlPanel" />
					</rich:inputNumberSlider>
					<a4j:poll interval="#{VideoPreview.refreshInterval}"
						reRender="videoPreviewInput,videoPreviewFilter" />
				</h:form>
			</rich:panel>
			<h:outputText value="Not available"
				rendered="#{not VideoPreview.available}" />
		</rich:panel>

	</f:view>

</ui:composition>
