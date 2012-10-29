<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<rich:panel>
		<f:facet name="control">
			<h:outputText value="Left/Right Motors Control" />
		</f:facet>
		<h:panelGrid columns="4">
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="leftMotorSpeed">
					<h:outputText value="Left Motor Speed" />
					<rich:inputNumberSlider minValue="-127"
						value="#{MotorView.leftMotorSpeed}" maxValue="127" step="1"
						width="400">
						<a4j:support event="onchange" reRender="leftMotorSpeed" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="rightMotorSpeed">
					<h:outputText value="Right Motor Speed" />
					<rich:inputNumberSlider minValue="-127" value="#{MotorView.rightMotorSpeed}" maxValue="127" step="1" width="400">
						<a4j:support event="onchange" reRender="rightMotorSpeed" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:form id="motorControl">
				<h:commandButton image="../images/motors/gear_run.png"
					actionListener="#{MotorView.processAction}" id="goMotors" value="Go" />
				<h:commandButton image="../images/motors/stop2.png"
					actionListener="#{MotorView.processAction}" id="resetMotors" value="Stop" />
			</h:form>

		</h:panelGrid>
	</rich:panel>
	<br />

	<rich:panel>
		<f:facet name="motionParameters">
			<h:outputText value="Speed/Acceleration" />
		</f:facet>
		<h:panelGrid columns="2">
			<c:forEach var="item" begin="0"
				end="#{MotorView.motionParameterCount - 1}">
				<rich:panel>
					<h:outputText value="#{MotorView.motionParametersData[item].name}" />
					<h:panelGrid columns="4">
						<h:panelGrid columns="2" border="0"
							style="border: 1px solid black;">
							<h:form id="speed#{item}">
								<h:outputText value="Speed" />
								<rich:inputNumberSlider minValue="0"
									value="#{MotorView.motionParametersData[item].speed}"
									maxValue="255" step="1" width="400">
									<a4j:support event="onchange" reRender="speed" />
								</rich:inputNumberSlider>
							</h:form>
						</h:panelGrid>
						<h:panelGrid columns="2" border="0"
							style="border: 1px solid black;">
							<h:form id="acceleration#{item}">
								<h:outputText value="Acceleration" />
								<rich:inputNumberSlider minValue="0"
									value="#{MotorView.motionParametersData[item].acceleration}"
									maxValue="255" step="1" width="400">
									<a4j:support event="onchange" reRender="acceleration" />
								</rich:inputNumberSlider>
							</h:form>
						</h:panelGrid>
					</h:panelGrid>
				</rich:panel>
			</c:forEach>
			<h:form>
				<a4j:commandButton image="../images/arrows/refresh.png"
					value="refresh" action="#{MotorView.refreshMotionParameters}"
					reRender="motionParameters" />
			</h:form>
		</h:panelGrid>
	</rich:panel>
	<br />
	<h:form>
		<h:selectBooleanCheckbox id="debugMotion" value="#{MotorView.debugMotion}"/>
		<h:outputText value="debug Motion" />
		<h:commandButton action="#{MotorView.doDebugMotion}" value="Test Debug motion" />
	</h:form>
	
	<br />
	<rich:panel>
		<f:facet name="header">
			<h:outputText value="Go/Rotate" />
		</f:facet>
		<h:panelGrid columns="4">
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="move">
					<h:outputText value="Move (mm)" />
					<rich:inputNumberSlider minValue="0" value="#{MotorView.move}"
						maxValue="2000" step="10" width="400">
						<a4j:support event="onchange" reRender="move" />
					</rich:inputNumberSlider>
					<br />
					<h:commandButton image="../images/arrows/arrow_up_green.png"
						action="#{MotorView.doForward}" value="Forward" />
					<h:commandButton image="../images/arrows/arrow_down_green.png"
						action="#{MotorView.doBackward}" value="Backward" />
					<h:commandButton image="../images/motors/stop2.png"
						action="#{MotorView.doStop}" value="Stop" />
				</h:form>
			</h:panelGrid>
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="rotation">
					<h:outputText value="Rotation (degree)" />
					<rich:inputNumberSlider minValue="0" value="#{MotorView.rotate}"
						maxValue="360" step="1" width="400">
						<a4j:support event="onchange" reRender="rotation" />
					</rich:inputNumberSlider>
					<br />
					<h:commandButton image="../images/arrows/nav_undo_blue.png"
						action="#{MotorView.doRotationLeft}" value="Rotate Left" />
					<h:outputText value=" " />
					<h:commandButton image="../images/arrows/nav_redo_blue.png"
						action="#{MotorView.doRotationRight}" value="Rotate Right" />
					<h:commandButton image="../images/motors/stop2.png"
						action="#{MotorView.doStop}" value="Stop" />
				</h:form>
				<h:form id="rotationOneWheel">
					<h:outputText value="Rotation on One Wheel (degree)" />
					<rich:inputNumberSlider minValue="-360"
						value="#{MotorView.rotateOneWheel}" maxValue="360" step="1"
						width="400">
						<a4j:support event="onchange" reRender="rotation" />
					</rich:inputNumberSlider>
					<br />
					<h:commandButton image="../images/arrows/nav_undo_blue.png"
						action="#{MotorView.doRotationOneWheelLeft}"
						value="Rotate One Wheel Left" />
					<h:outputText value=" " />
					<h:commandButton image="../images/arrows/nav_redo_blue.png"
						action="#{MotorView.doRotationOneWheelRight}"
						value="Rotate One Wheel Right" />
					<h:commandButton image="../images/motors/stop2.png"
						action="#{MotorView.doStop}" value="Stop" />
				</h:form>
			</h:panelGrid>
		</h:panelGrid>
	</rich:panel>

	<rich:panel id="position">
		<f:facet name="header">
			<h:outputText value="Position in pulse" />
		</f:facet>
		<h:panelGrid columns="5" border="0" style="border: 1px solid black;">
			<h:outputLabel value="Left Position :" />
			<h:outputLabel value="#{MotorView.left}" />
			<h:outputLabel value="Right Position :" />
			<h:outputLabel value="#{MotorView.right}" />
			<h:form>
				<a4j:commandButton image="../images/arrows/refresh.png"
					value="refresh" action="#{MotorView.refreshPosition}"
					reRender="position" />
			</h:form>
		</h:panelGrid>
	</rich:panel>

	<rich:panel id="absolutePosition">
		<f:facet name="header">
			<h:outputText value="Absolute position" />
		</f:facet>
		<h:panelGrid columns="7" border="0" style="border: 1px solid black;">
			<h:outputLabel value="X Position (mm) :" />
			<h:outputLabel value="#{MotorView.positionX}" />
			<h:outputLabel value="Y Position (mm) :" />
			<h:outputLabel value="#{MotorView.positionY}" />
			<h:outputLabel value="Alpha Position (deg) :" />
			<h:outputLabel value="#{MotorView.alpha}" />
			<h:form>
				<a4j:commandButton image="../images/arrows/refresh.png"
					value="refresh" action="#{MotorView.refreshAbsolutePosition}"
					reRender="absolutePosition" />
				<a4j:commandButton image="../images/motors/stop2.png"
					value="refresh" action="#{MotorView.clearAbsolutePosition}"
					reRender="absolutePosition" />
			</h:form>
		</h:panelGrid>
	</rich:panel>

	<rich:panel id="commandsPanel">
		<f:facet name="header">
			<h:outputText value="Commands" />
		</f:facet>
		<a4j:form>
			<h:outputLabel value="Distance (mm): " for="gameboardDistance" />
			<h:inputText id="gameboardDistance" value="#{MotorView.distance}">
				<a4j:support event="onchange" reRender="commandsPanel" />
			</h:inputText>
			<h:outputLabel value="Command: " for="moveCommandString" />
			<h:outputText id="moveCommandString" value="#{MotorView.moveData}" />
			<br />
			<h:outputLabel value="Angle (°): " for="gameboardAngle" />
			<h:inputText id="gameboardAngle" value="#{MotorView.angle}">
				<a4j:support event="onchange" reRender="commandsPanel" />
			</h:inputText>
			<h:outputLabel value="Command: " for="rotateCommandString" />
			<h:outputText id="rotateCommandString"
				value="#{MotorView.rotateData}" />
			<br />
			<h:outputLabel value="Pulses count: " for="motorPulses" />
			<h:inputText id="motorPulses" value="#{MotorView.pulses}">
				<a4j:support event="onchange" reRender="commandsPanel" />
			</h:inputText>
			<h:outputLabel value="Distance: " for="distanceString" />
			<h:outputText id="distanceString" value="#{MotorView.distanceData}" />
		</a4j:form>
	</rich:panel>

	<rich:panel id="calibrationPanel">
		<f:facet name="header">
			<h:outputText value="Calibration" />
		</f:facet>
		<a4j:form>
			<a4j:commandButton value="Calibrate" action="#{MotorView.calibrate}" />
		</a4j:form>
	</rich:panel>

</ui:composition>
