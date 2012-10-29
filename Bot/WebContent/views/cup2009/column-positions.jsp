<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>
		<a4j:form>
			<h:outputLabel for="columnElementPositionsConfigurationId">Column elements placement card: </h:outputLabel>
			<h:selectOneMenu id="columnElementPositionsConfigurationId"
				value="#{ColumnElementPositionsView.columnElementsConfigurationId}"
				onchange="updateGameboard()">
				<f:selectItem itemLabel="none" itemValue="0" />
				<c:forEach var="i" begin="1" end="10">
					<f:selectItem itemLabel="#{i}/10" itemValue="#{i}" />
				</c:forEach>
			</h:selectOneMenu>
			<br />
			<h:outputLabel for="dispensersConfigurationId">Dispensers placement card: </h:outputLabel>
			<h:selectOneMenu id="dispensersConfigurationId"
				value="#{ColumnElementPositionsView.dispensersConfigurationId}"
				onchange="updateGameboard()">
				<f:selectItem itemLabel="none" itemValue="0" />
				<c:forEach var="i" begin="11" end="12">
					<f:selectItem itemLabel="#{i}/10" itemValue="#{i}" />
				</c:forEach>
			</h:selectOneMenu>
			<br />
			<h:selectBooleanCheckbox id="firstTrajectory"
				value="#{ColumnElementPositionsView.trajectory[0]}"
				onchange="updateGameboard()" />
			<h:outputLabel for="firstTrajectory">Display 1st trajectory (4 elements)</h:outputLabel>
			<br />
			<h:selectBooleanCheckbox id="secondTrajectory"
				value="#{ColumnElementPositionsView.trajectory[1]}"
				onchange="updateGameboard()" />
			<h:outputLabel for="secondTrajectory">Display 2nd trajectory (2 elements)</h:outputLabel>
			<br />
			<h:selectBooleanCheckbox id="thirdTrajectory"
				value="#{ColumnElementPositionsView.trajectory[2]}"
				onchange="updateGameboard()" />
			<h:outputLabel for="thirdTrajectory">Display 3rd trajectory (1 lintel)</h:outputLabel>
			<p /><h:outputText id="estimatedTime"
				value="Estimated time: #{ColumnElementPositionsView.estimatedTime}" />
			<p /><h:outputText
				value="Trajectory of configuration #{ColumnElementPositionsView.trajectoryCardId}:"
				rendered="#{ColumnElementPositionsView.trajectoryCardId > 0}" /><br />
			<h:outputText id="trajectoryData" escape="false"
				value="#{ColumnElementPositionsView.trajectoryData}" /><a4j:jsFunction
				name="updateGameboard"
				reRender="gameBoardImage,estimatedTime,trajectoryData"
				action="#{ColumnElementPositionsView.updateView}" />
		</a4j:form>
	</f:view>

</ui:composition>