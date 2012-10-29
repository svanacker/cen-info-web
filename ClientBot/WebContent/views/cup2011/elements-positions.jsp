<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>

		<a4j:form id="elementsPositionsForm">
			<h:panelGrid columns="#{ElementsPositionsView.columns}">

				<c:forEach var="y" begin="0" end="#{ElementsPositionsView.rows - 1}">
					<c:forEach var="x" begin="0"
						end="#{ElementsPositionsView.columns - 1}">
						<h:column>
							<h:outputText value="#{x} #{y}" />
							<h:selectBooleanCheckbox
								value="#{ElementsPositionsView.elements[x][y]}">
								<a4j:support action="#{ElementsPositionsView.updateElements}"
									event="onchange"
									reRender="gameBoardImage,elementsPositionsForm" />
							</h:selectBooleanCheckbox>
						</h:column>
					</c:forEach>
				</c:forEach>

			</h:panelGrid>
		</a4j:form>

	</f:view>

</ui:composition>