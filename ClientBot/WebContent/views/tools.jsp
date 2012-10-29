<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<rich:panel>
		<f:facet name="header">
			<h:outputText value="Motors computation" />
		</f:facet>
		<h:panelGrid columns="4">
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="rotationByMinute">
					<h:outputText value="Motor Rotation By Minute"/>
					<rich:inputNumberSlider minValue="0"
						value="#{MotorComputeView.motorRotationByMinute}" maxValue="10000" step="200" width="400">
						<a4j:support event="onchange" reRender="rotationByMinute" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="motorTorque">
					<h:outputText value="Motor Torque (N m)" />
					<rich:inputNumberSlider minValue="0"
						value="#{MotorComputeView.motorTorque}" maxValue="0.5" step="0.001" width="400">
						<a4j:support event="onchange" reRender="motorTorque" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="reductionFactor">
					<h:outputText value="Reduction Factor" />
					<rich:inputNumberSlider minValue="0"
						value="#{MotorComputeView.reductionFactor}" maxValue="1000" step="1" width="400">
						<a4j:support event="onchange" reRender="reductionFactor" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:form id="tools">		
				<h:commandButton image="../images/motors/gear_run.png" id="goMotors" value="Go" />
			</h:form>
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="efficiency">
					<h:outputText value="Reductor efficiency"/>
					<rich:inputNumberSlider minValue="0"
						value="#{MotorComputeView.efficiency}" maxValue="1" step="0.01" width="400">
						<a4j:support event="onchange" reRender="rotationByMinute" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="robotWeight">
					<h:outputText value="Robot weight (kg)"/>
					<rich:inputNumberSlider minValue="0"
						value="#{MotorComputeView.robotWeight}" maxValue="20" step="0.5" width="400">
						<a4j:support event="onchange" reRender="robotWeight" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="wheelDiameter">
					<h:outputText value="Wheel Diameter (meter)"/>
					<rich:inputNumberSlider minValue="0"
						value="#{MotorComputeView.wheelDiameter}" maxValue="0.2" step="0.0001" width="400">
						<a4j:support event="onchange" reRender="wheelDiameter" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
				<h:form id="encoderCounter">
					<h:outputText value="Encoder counter (nominal)"/>
					<rich:inputNumberSlider minValue="0"
						value="#{MotorComputeView.encoderCounter}" maxValue="1000" step="100" width="400">
						<a4j:support event="onchange" reRender="encoderCounter" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
		</h:panelGrid>
		<h:panelGrid columns="2" border="0">
			<h:outputText value="Rotation By Minute at reductor : "/>
			<h:outputText value="#{MotorComputeView.rotationByMinuteAtReductor} rotation / min" />
			<h:outputText value="Rotation By Second at reductor :"/>
			<h:outputText value="#{MotorComputeView.rotationBySecondAtReductor} rotation / s"/>
			<h:outputText value="Time to do one rotation at reductor :"/>
			<h:outputText value="#{MotorComputeView.timeForOneRotationAtReductor} second"/>
			<h:outputText value="Time to do 40 degree at reductor : "/>
			<h:outputText value="#{MotorComputeView.timeFor40degreeAtReductor} second"/>			
			<h:outputText value="Max Speed of the robot : "/>
			<h:outputText value="#{MotorComputeView.robotMaxSpeed} meter / second"/>
			<h:outputText value="Max encoder frequency for a wheel : "/>
			<h:outputText value="#{MotorComputeView.rotationBySecondAtReductor * MotorComputeView.encoderCounter * 4} Hz"/>
			<h:outputText value="Min sample frequency to decode both motors : "/>
			<h:outputText value="#{MotorComputeView.rotationBySecondAtReductor * MotorComputeView.encoderCounter * 4 * 4} Hz"/>
			<h:outputText value="Precision of the robot : "/>
			<h:outputText value="#{MotorComputeView.wheelDiameter * 1000 * 3.15169 / (MotorComputeView.encoderCounter * 4)} mm"/>			
			<h:outputText value="Reductor torque : "/>
			<h:outputText value="#{MotorComputeView.reductorTorque} Newton meter"/>
			<h:outputText value="Force at the limit of the wheel :"/>
			<h:outputText value="#{MotorComputeView.forceAtTheWheel} Newton"/>
			<h:outputText value="Robot Acceleration :"/>
			<h:outputText value="#{MotorComputeView.robotMaxAcceleration} meter s-2"/>
			<h:outputText value="Time to reach max Speed :"/>
			<h:outputText value="#{MotorComputeView.timeToReachMaxSpeed} second"/>
			<h:outputText value="Distance when reach max Speed :"/>
			<h:outputText value="#{MotorComputeView.distanceWhenReachMaxSpeed} meter"/>
			<h:outputText value="Time to reach 1 m / s :"/>
			<h:outputText value="#{MotorComputeView.timeToReachOneMeterBySecond} second"/>
			<h:outputText value="Time to reach 2 m with trapezoidal :"/>
			<h:outputText value="#{MotorComputeView.timeToReachOneMeter} second"/>
		</h:panelGrid>			
	</rich:panel>
	<br/>	
</ui:composition>
