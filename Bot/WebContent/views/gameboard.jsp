<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:verbatim>
		<script type="text/javascript" language="Javascript">
		<!--
			function setClickCoords(c, e) {
				var x = e.clientX + document.body.scrollLeft;
				var y = e.clientY + document.body.scrollTop;
				var p = c;
				while (p) {
					x -= p.offsetLeft;
					y -= p.offsetTop;
					p = p.offsetParent;
				}
				document.getElementById('gameBoardForm:clickX').value = x;
				document.getElementById('gameBoardForm:clickY').value = y;
			};
		//-->
		</script>
	</f:verbatim>
	<f:view>
		<h:panelGrid columns="2">

			<rich:panel>
				<h:form id="gameBoardForm">
					<h:inputHidden id="clickX" value="#{GameBoardView.x}" />
					<h:inputHidden id="clickY" value="#{GameBoardView.y}" />
					<a4j:commandLink actionListener="#{GameBoardView.processAction}"
						reRender="gameBoardImage,gameBoardTrajectoryCommand,gameBoardDestination">
						<rich:paint2D id="gameBoardImage" width="400" height="500"
							format="png" paint="#{GameBoardView.paint}"
							data="#{GameBoardView.n}" onclick="setClickCoords(this,event);"
							border="0" />
					</a4j:commandLink>

					<p />

					<rich:panel>

						<h:selectBooleanCheckbox
							value="#{GameBoardView.trajectoryAutoUpdate}">
							<a4j:support event="onchange" reRender="gameBoardImage" />Mise à jour automatique</h:selectBooleanCheckbox>

						<p />

						<h:outputText value="Final orientation (°):" />
						<rich:inputNumberSlider value="#{GameBoardView.finalAngle}"
							minValue="-180" maxValue="180" step="1">
							<a4j:support event="onchange"
								reRender="gameBoardImage,gameBoardTrajectoryCommand"
								action="#{GameBoardView.updateTrajectory}"
								rendered="#{GameBoardView.trajectoryAutoUpdate}" />
						</rich:inputNumberSlider>
						<br />
						<h:outputText value="Control point 1 distance (mm):" />
						<rich:inputNumberSlider value="#{GameBoardView.cp1Distance}"
							minValue="-1200" maxValue="1200" step="10">
							<a4j:support event="onchange"
								reRender="gameBoardImage,gameBoardTrajectoryCommand"
								action="#{GameBoardView.updateTrajectory}"
								rendered="#{GameBoardView.trajectoryAutoUpdate}" />
						</rich:inputNumberSlider>
						<br />
						<h:outputText value="Control point 2 distance (mm):" />
						<rich:inputNumberSlider value="#{GameBoardView.cp2Distance}"
							minValue="-1200" maxValue="1200" step="10">
							<a4j:support event="onchange"
								reRender="gameBoardImage,gameBoardTrajectoryCommand"
								action="#{GameBoardView.updateTrajectory}"
								rendered="#{GameBoardView.trajectoryAutoUpdate}" />
						</rich:inputNumberSlider>
						<br />

						<a4j:commandButton image="../images/arrows/refresh.png"
							value="Refresh"
							reRender="gameBoardImage,gameBoardTrajectoryCommand" />
						<h:commandButton image="../images/motors/gear_run.png"
							action="#{GameBoardView.doBezier}" id="bezierGo" value="Go" />
						<a4j:commandButton action="#{GameBoardView.moveToFinalPosition}"
							id="gameboardViewMoveToFinalPosition" value="Move robot"
							reRender="gameBoardImage,gameBoardTrajectoryCommand" />

						<p />

						<h:outputText id="gameBoardDestination"
							value="Destination: #{GameBoardView.trajectoryDestination}" />
						<br />
						<h:outputText id="gameBoardTrajectoryCommand"
							value="Command: #{GameBoardView.trajectoryCommand}" />

					</rich:panel>

					<p />

					<rich:panel>
						<h:selectBooleanCheckbox value="#{GameBoardView.highlightTargets}">
							<a4j:support event="onchange" reRender="gameBoardImage" />Afficher les
					cibles</h:selectBooleanCheckbox>
						<br />
						<h:selectBooleanCheckbox
							value="#{GameBoardView.highlightObstacles}">
							<a4j:support event="onchange" reRender="gameBoardImage" />Afficher les
					obstacles</h:selectBooleanCheckbox>
						<br />
						<h:selectBooleanCheckbox value="#{GameBoardView.highlightPaths}">
							<a4j:support event="onchange" reRender="gameBoardImage" />Afficher les chemins</h:selectBooleanCheckbox>
						<br />
						<h:selectBooleanCheckbox value="#{GameBoardView.displayWeights}">
							<a4j:support event="onchange" reRender="gameBoardImage" />Afficher les poids</h:selectBooleanCheckbox>
						<br />
						<h:selectBooleanCheckbox value="#{GameBoardView.displayLabels}">
							<a4j:support event="onchange" reRender="gameBoardImage" />Afficher les libellés</h:selectBooleanCheckbox>
					</rich:panel>

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
					<rich:tab id="elementsPositionsTab" label="Eléments">
						<ui:include src="cup2012/elements-positions.jsp" />
					</rich:tab>
					<rich:tab id="trajectoryTab" label="Trajectoire">
						<ui:include src="trajectoryBuilder.jsp" />
					</rich:tab>
				</rich:tabPanel>
			</rich:panel>

		</h:panelGrid>
	</f:view>

</ui:composition>
