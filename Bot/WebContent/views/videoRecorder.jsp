<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>
		<rich:panel id="videoRecorderPanel">
			<h:form>
				<h:outputLabel for="videoRecorderDestination" value="Destination:" />
				<h:inputText id="videoRecorderDestination" size="120"
					value="#{VideoRecorderView.destination}" />
				<p /><a4j:commandButton value="#{VideoRecorderView.text}"
					action="#{VideoRecorderView.execute}" reRender="videoRecorderPanel" />
			</h:form>
			<p /><h:outputText value="Status: #{VideoRecorderView.status}" />
		</rich:panel>
	</f:view>

</ui:composition>
