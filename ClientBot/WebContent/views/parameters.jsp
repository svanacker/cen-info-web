<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">
		
	<link href="#{facesContext.externalContext.requestContextPath}/css/parameters.css" rel="stylesheet" type="text/css"/>
	
	<rich:panel id="parametersPanel">
		<h:form>
			<h:outputLabel for="beaconRefreshRate" value="beaconRefreshRate:" />
			<h:inputText id="beaconRefreshRate" value="#{ParametersView.beaconRefreshRate}" />   (time in miliseconds)
			<p />
			<h:outputLabel for="readSpeed" value="readSpeed:" />
			<h:inputText id="readSpeed" value="#{ParametersView.readSpeed}" />   (int multiplier)
			<p />
			
			<!--  
		
		<h:outputLabel for="host" value="host:" />   
			<h:inputText id="host" value="#{ParametersView.host}" />  (dns ou ip)
			<p />
			<h:outputLabel for="robotSpeed" value="robotSpeed:" />
			<h:inputText id="robotSpeed" value="#{ParametersView.robotSpeed}" />   (speed in ms-1)
			<p />
			<h:outputLabel for="robotRefreshRate" value="robotRefreshRate:" />
			<h:inputText id="robotRefreshRate" value="#{ParametersView.robotRefreshRate}" />   (distance in milimeters)
			<p />
			<h:outputLabel for="opponentSpeed" value="opponentSpeed:" />
			<h:inputText id="opponentSpeed" value="#{ParametersView.opponentSpeed}" />  (speed in ms-1)
			<p />
			<h:outputLabel for="configuration" value="configuration:" />
			<h:inputText id="configuration" value="#{ParametersView.configuration}" />  (int between 0 and 255)
			<p />
		-->
			
			<a4j:commandButton value="send" action="#{ParametersView.sendData}" reRender="parametersPanel" />
		</h:form>
	</rich:panel>
</ui:composition>
	