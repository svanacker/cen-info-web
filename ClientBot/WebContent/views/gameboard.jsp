<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">


	<f:view>
		<h:panelGrid columns="2">
 
			<rich:panel>
				<h:form id="gameBoardForm">
					<h:inputHidden id="clickX" value="#{GameBoardView.x}" />
					<h:inputHidden id="clickY" value="#{GameBoardView.y}" />
				
						<rich:paint2D id="gameBoardImage" width="400" height="500"
							format="png" paint="#{GameBoardView.paint}"
							data="#{GameBoardView.n}" border="0" />

					<p />
					<h:panelGrid columns="2">
						<rich:panel>
							<a4j:commandButton image="../images/arrows/refresh.png"
								value="Refresh" reRender="gameBoardImage" />
						</rich:panel>
						<rich:panel>
							<h:selectBooleanCheckbox
								value="#{GameBoardView.highlightObstacles}">
								<a4j:support event="onchange" reRender="gameBoardImage" />Afficher les
					obstacles</h:selectBooleanCheckbox>
							<br />
						</rich:panel>
					</h:panelGrid>
				</h:form>
				<p />
			</rich:panel>
						
			<rich:panel rendered="#{not empty(GameBoardView.consoles)}">			
				<rich:tabPanel switchType="client">
					<c:forEach var="console" items="#{GameBoardView.consoles}">
						<rich:tab label="#{console.name}"
							id="#{console.name}ConsoleTabPanel">
							<h:form id="#{console.name}">
								<c:forEach var="action" items="#{console.actions}">
									<p />
									<a4j:commandLink
										actionListener="#{GameBoardView.processConsoleAction}"
										id="#{action.key}" action="click" value="#{action.value}"
										reRender="#{console.name}ConsoleTabPanel" />
								</c:forEach>
							</h:form>
							<p />
							<c:forEach var="property" items="#{console.properties}">
								<br />
								<h:outputText value="#{property.key}=#{property.value}" />
							</c:forEach>
							<p />
							<rich:paint2D width="300" height="300" format="png"
								paint="#{GameBoardView.drawGraph}" border="0"
								data="#{console.name}:#{GameBoardView.n}" cacheable="false" />
						</rich:tab>
					</c:forEach>
				</rich:tabPanel>
			</rich:panel>
		</h:panelGrid>
		
	</f:view>

</ui:composition>
