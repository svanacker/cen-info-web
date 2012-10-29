<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>
		<h:panelGrid columns="2">
			<rich:panel id="comPanel">
				<h:form>
					<h:outputLabel for="comInputText" value="simulate Input From Microcontroller:"  />
					<h:inputText id="comInputText" value="#{ComView.inputFromMicrocontroller}" size="80" />
					<a4j:commandButton value="send" action="#{ComView.simulateDataFromMicrocontroller}"
						reRender="comPanel" />
				</h:form>
			
				<h:form>
					<h:outputLabel for="comInputText" value="Data to send:" />
					<h:inputText id="comInputText" value="#{ComView.input}" size="60" />
					<h:selectBooleanCheckbox id="comWaitForAck"
						value="#{ComView.waitForAck}" />
					<h:outputLabel for="comWaitForAck" value="Wait for ack" />
					<a4j:commandButton value="send" action="#{ComView.sendData}"
						reRender="comPanel" />
				</h:form>

				<h2><h:outputText value="In Data" /></h2>
				<!-- In Data -->
				<h:dataTable value="#{ComView.inDataList}" var="record" border="1">
					<h:column>
						<f:facet name="header">
							<h:outputText value="Date" />
						</f:facet>
						<h:outputText value="#{record.creationDateAsText}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="InData" />
						</f:facet>
						<h:outputText value="#{record.description}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Message" />
						</f:facet>
						<h:outputText value="#{record}" />
					</h:column>
				</h:dataTable>

				<!-- Out Data -->
				<h2><h:outputText value="Out Data" /></h2>
				<h:dataTable value="#{ComView.outDataList}" var="record" border="1">
					<h:column>
						<f:facet name="header">
							<h:outputText value="OutData" />
						</f:facet>
						<h:outputText value="#{record.creationDateAsText}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Description" />
						</f:facet>
						<h:outputText value="#{record.description}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Message" />
						</f:facet>
						<h:outputText value="#{record.message}" />
					</h:column>
				</h:dataTable>
				<br></br>

				<h2><h:outputText value="Headers" /></h2>
				<h:form id="outDataHeadersForm">
					<p />
					<a4j:commandButton id="headersPopulateButton" value="Populate"
						action="#{ComView.populateDescriptors}"
						reRender="outDataDescriptorsTable,outDataHeadersForm"
						rendered="#{empty ComView.outDataDescriptors}" />
					<p />
				</h:form>
				<h:dataTable id="outDataDescriptorsTable"
					value="#{ComView.outDataDescriptors}" var="descriptor" border="1">
					<h:column>
						<f:facet name="header">
							<h:outputText value="Cup" />
						</f:facet>
						<h:outputText value="#{descriptor.cup}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Name" />
						</f:facet>
						<h:outputText value="#{descriptor.name}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Header" />
						</f:facet>
						<h:outputText value="#{descriptor.header}" />
					</h:column>
				</h:dataTable>
				<br />

				<!-- Raw Data -->
				<h2><h:outputText value="Raw Data" /></h2>
				<h:dataTable value="#{ComView.rawInDataList}" var="record"
					border="1">
					<h:column>
						<f:facet name="header">
							<h:outputText value="Date" />
						</f:facet>
						<h:outputText value="#{record.creationDateAsText}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="RawInData" />
						</f:facet>
						<h:outputText value="#{record.description}" />
					</h:column>
				</h:dataTable>

				<rich:spacer height="10" />

				<h:form>
					<a4j:commandButton image="../images/arrows/refresh.png"
						value="Refresh" reRender="comPanel" />
					<a4j:commandButton image="../images/motors/stop2.png" value="Clear"
						action="#{ComView.clear}" reRender="comPanel" />
					<a4j:commandButton value="Reinitialize"
						action="#{ComView.reconnect}" />
				</h:form>
			</rich:panel>
			<rich:panel id="comStatementsPanel">
				<h:form>
					<h:selectOneListbox value="#{ComView.currentStatement}" size="30">
						<f:selectItems value="#{ComView.statements}" />
					</h:selectOneListbox>
					<a4j:commandButton value="Send" action="#{ComView.sendStatement}"
						reRender="comPanel,comStatementsPanel" />
				</h:form>
			</rich:panel>
		</h:panelGrid>
	</f:view>

</ui:composition>
