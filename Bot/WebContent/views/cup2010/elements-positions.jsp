<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>
		<a4j:form>
			<h:outputLabel for="elementsPositionsConfigurationCard1">Elements placement card #1: </h:outputLabel>
			<h:selectOneMenu id="elementsPositionsConfigurationCard1"
				value="#{ElementsPositionsView.elementsConfigurationCard1}"
				onchange="updateGameboard()">
				<f:selectItem itemLabel="none" itemValue="0" />
				<c:forEach var="i" begin="1" end="9">
					<f:selectItem itemLabel="#{i}/9" itemValue="#{i}" />
				</c:forEach>
			</h:selectOneMenu>
			<br />
			<h:outputLabel for="elementsPositionsConfigurationCard2">Elements placement card #2: </h:outputLabel>
			<h:selectOneMenu id="elementsPositionsConfigurationCard2"
				value="#{ElementsPositionsView.elementsConfigurationCard2}"
				onchange="updateGameboard()">
				<f:selectItem itemLabel="none" itemValue="0" />
				<c:forEach var="i" begin="1" end="4">
					<f:selectItem itemLabel="#{i}/4" itemValue="#{i}" />
				</c:forEach>
			</h:selectOneMenu>
			<br />
			<p /><h:outputText value="Opponent's positions" /><rich:dataTable
				value="#{ElementsPositionsView.opponentPositions}" var="position"
				border="1" id="opponentsPositions">
				<rich:column>
					<f:facet name="header">
						<h:outputText value="Timestamp" />
					</f:facet>
					<h:outputText value="#{position.date}">
						<f:convertDateTime dateStyle="short" type="both" />
					</h:outputText>
				</rich:column>
				<rich:column>
					<f:facet name="header">
						<h:outputText value="Location" />
					</f:facet>
					<h:outputText value="#{position.location.x}">
						<f:convertNumber pattern="(0',' " />
					</h:outputText>
					<h:outputText value="#{position.location.y}">
						<f:convertNumber pattern=" 0)" />
					</h:outputText>
				</rich:column>
				<rich:column>
					<f:facet name="header">
						<h:outputText value="Direction" />
					</f:facet>
					<h:outputText value="#{position.direction}">
						<f:convertNumber pattern="0.0°" />
					</h:outputText>
				</rich:column>
				<rich:column>
					<f:facet name="header">
						<h:outputText value="Actions" />
					</f:facet>
					<a4j:commandLink value="remove" action="#{position.remove}"
						reRender="opponentsPositions"></a4j:commandLink>
				</rich:column>
			</rich:dataTable> <a4j:jsFunction name="updateGameboard" reRender="gameBoardImage"
				action="#{ElementsPositionsView.updateView}" /> <a4j:push
				eventProducer="#{ElementsPositionsView.addOpponentPositionsListener}"
				reRender="opponentsPositions" interval="1000" />
		</a4j:form>
	</f:view>

</ui:composition>