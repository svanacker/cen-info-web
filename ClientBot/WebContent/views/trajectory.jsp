<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>
		<h:form>
			<rich:toolBar id="trajectoryToolbar">
				<a4j:commandButton value="Record" reRender="trajectoryToolbar"
					action="#{TrajectoryView.start}"
					disabled="#{TrajectoryView.recording}" />
				<a4j:commandButton reRender="trajectoryList,trajectoryToolbar"
					value="Stop" action="#{TrajectoryView.stop}"
					disabled="#{not TrajectoryView.recording}" />
				<a4j:commandButton reRender="trajectoryList" value="Delete"
					action="#{TrajectoryView.delete}" />
			</rich:toolBar>
			<p /><rich:dataTable id="trajectoryList"
				value="#{TrajectoryView.records}" var="record">
				<rich:column width="20">
					<f:facet name="header">
						<h:outputText value="" />
					</f:facet>
					<h:selectBooleanCheckbox value="#{record.active}">
						<a4j:support event="onclick" />
					</h:selectBooleanCheckbox>
				</rich:column>
				<rich:column width="150">
					<f:facet name="header">Creation time</f:facet>
					<h:outputText value="#{record.startDate}" />
				</rich:column>
				<rich:column>
					<f:facet name="header">Length (s)</f:facet>
					<h:outputText value="#{record.length}" />
				</rich:column>
			</rich:dataTable>
		</h:form>
	</f:view>
</ui:composition>