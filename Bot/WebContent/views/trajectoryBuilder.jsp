<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>
		<a4j:form id="trajectoryForm">
			<h:outputLabel for="trajectoryValue">Trajectory: </h:outputLabel>
			<h:inputText id="trajectoryValue" size="50"
				value="#{TrajectoryBuilder.trajectory}">
			</h:inputText>
			<a4j:commandButton value="Ok"
				reRender="gameBoardImage,trajectoryForm" />
			<p />
			<h:outputLabel for="displayedTrajectoryValue">Displayed trajectory: </h:outputLabel>
			<h:outputText id="displayedTrajectoryValue"
				value=" #{TrajectoryBuilder.displayedTrajectory}" />
			<br />
			<p />
			<h:outputLabel for="displayedTrajectoryOutData">Trajectory data:</h:outputLabel>
			<br />
			<h:outputText id="displayedTrajectoryOutData" escape="false"
				value=" #{TrajectoryBuilder.trajectoryOutData}" />
			<br />
		</a4j:form>
	</f:view>

</ui:composition>