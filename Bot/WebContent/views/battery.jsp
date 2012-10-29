<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<rich:panel id="battery">
		<f:facet name="header">
			<h:outputText value="Battery" />
		</f:facet>	
		<h:outputText value="Battery Voltage : #{BatteryView.voltage} V" />

		<c:if test="${BatteryView.batteryState == 0}">
			<h:graphicImage value="../images/battery/battery_ok.png"/>
		</c:if>
		<c:if test="${BatteryView.batteryState == 1}">
			<h:graphicImage value="../images/battery/battery_warning.png"/>
		</c:if>
	</rich:panel>

</ui:composition>
