<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<rich:panel id="relayCountPanel">
		<h:form>
			<h:outputLabel for="selectCount" value="Nombre de relais :" />
			<h:selectOneMenu id="selectCount" value="#{RelayView.count}">
				<c:forEach var="item" begin="1" end="#{RelayView.maxCount}">
					<f:selectItem itemValue="#{item}" />
				</c:forEach>
				<a4j:support event="onchange" reRender="relayPanel,relayCountPanel" />
			</h:selectOneMenu>
		</h:form>
	</rich:panel>
	<rich:panel id="relayPanel">
		<c:forEach var="item" begin="1" end="#{RelayView.count}">
			<h:form id="relay#{item}">
				<h:selectBooleanCheckbox id="relay#{item}CheckBox"
					value="#{RelayView.data[item-1].value}"
					onchange="document.getElementById('relay#{item}').submit();" />
				<h:outputLabel for="relay#{item}CheckBox"
					value="Relay Activated #{item}" />
			</h:form>
		</c:forEach>
	</rich:panel>
</ui:composition>
