<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>

		<rich:tabPanel switchType="client">

			<rich:tab label="Adversaire">

				<a4j:form id="opponentPositionsForm">
					<h:panelGrid columns="#{OpponentPositionsView.columnsCount}">

						<c:forEach var="x" begin="0"
							end="#{OpponentPositionsView.rowsCount - 1}">
							<c:forEach var="y" begin="0"
								end="#{OpponentPositionsView.columnsCount - 1}">
								<h:column>
									<h:outputText
										value="#{OpponentPositionsView.elements[OpponentPositionsView.rowsCount - 1 - x][y].name}" />
									<h:selectBooleanCheckbox
										value="#{OpponentPositionsView.elements[OpponentPositionsView.rowsCount - 1 - x][y].value}">
										<a4j:support event="onchange"
											reRender="gameBoardImage,opponentPositionsForm" />
									</h:selectBooleanCheckbox>
								</h:column>
							</c:forEach>
						</c:forEach>

					</h:panelGrid>
				</a4j:form>

			</rich:tab>

			<rich:tab label="Pions">

				<a4j:form id="pawnsPositionsForm">
					<h:panelGrid columns="#{PawnsPositionsView.columnsCount}">

						<c:forEach var="x" begin="0"
							end="#{PawnsPositionsView.rowsCount - 1}">
							<c:forEach var="y" begin="0"
								end="#{PawnsPositionsView.columnsCount - 1}">
								<h:column>
									<h:outputText
										value="#{PawnsPositionsView.elements[PawnsPositionsView.rowsCount - 1 - x][y].name}" />
									<h:selectBooleanCheckbox
										value="#{PawnsPositionsView.elements[PawnsPositionsView.rowsCount - 1 - x][y].value}">
										<a4j:support event="onchange"
											reRender="gameBoardImage,pawnsPositionsForm" />
									</h:selectBooleanCheckbox>
								</h:column>
							</c:forEach>
						</c:forEach>

					</h:panelGrid>
				</a4j:form>

			</rich:tab>

		</rich:tabPanel>

	</f:view>

</ui:composition>