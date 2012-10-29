<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:verbatim>
		<style type="text/css">
.date {
	width: 150px;
}

tr:hover {
	color: blue;
	background: #FFF0F0;
}
</style>
	</f:verbatim>

	<f:view>
		<h:dataTable id="logDataTable" value="#{LogView.records}" var="record"
			columnClasses="date, name, message">
			<h:column>
				<f:facet name="header">
					<h:outputText value="Date / Heure" />
				</f:facet>
				<h:outputText value="#{record.dateAsString}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Module" />
				</f:facet>
				<h:outputText value="#{record.logRecord.loggerName}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Message" />
				</f:facet>
				<h:outputText value="#{record.logRecord.message}" />
				<h:outputText value=" #{record.logRecord.thrown}" />
			</h:column>
		</h:dataTable>

		<h:form>
			<a4j:commandButton image="../images/arrows/refresh.png"
				value="Refresh" reRender="logDataTable" />
			<a4j:commandButton image="../images/motors/stop2.png" value="Clear"
				reRender="logDataTable" action="#{LogView.clear}" />
		</h:form>

	</f:view>

</ui:composition>
