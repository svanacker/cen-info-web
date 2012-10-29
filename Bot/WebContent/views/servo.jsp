<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<rich:panel id="servoCountPanel">
		<h:form>
			<h:outputLabel for="selectCount" value="Nombre de servos :" />
			<h:selectOneMenu id="selectCount" value="#{ServoView.count}">
				<c:forEach var="item" begin="1" end="#{ServoView.maxCount}">
					<f:selectItem itemValue="#{item}" />
				</c:forEach>
				<a4j:support event="onchange" reRender="servoPanel,servoCountPanel" />
			</h:selectOneMenu>
		</h:form>
	</rich:panel>
	<rich:panel id="servoPanel">
		<c:forEach var="item" begin="1" end="#{ServoView.count}">
			<rich:panel>
				<f:facet name="header">
					<h:outputText value="Servo #{item}" />
				</f:facet>
				<h:form id="servo#{item}">
				<h:panelGrid columns="3" border="0" style="border: 1px solid black;">
						<h:outputText value="Position" />
						<rich:inputNumberSlider minValue="0"
							value="#{ServoView.data[item-1].value}" maxValue="4000" step="50">
							<a4j:support event="onchange" reRender="servo#{item}" />
						</rich:inputNumberSlider>
						<a4j:commandButton image="../images/motors/stop2.png" value="stop" action="#{ServoView.data[item-1].clear}" reRender="servo#{item}"/>
						<h:outputText value="Speed" />
						<rich:inputNumberSlider minValue="0"
							value="#{ServoView.data[item-1].speed}" maxValue="255" step="5">
							<a4j:support event="onchange" reRender="servo#{item}" />
						</rich:inputNumberSlider>
				</h:panelGrid>
				</h:form>
			</rich:panel>
		</c:forEach>
	</rich:panel>
	<br />
	<h:form>
		<h:commandButton action="close" value="Close" />
	</h:form>

</ui:composition>
