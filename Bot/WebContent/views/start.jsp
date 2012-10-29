<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:f="http://java.sun.com/jsf/core">
<body>
<ui:composition template="template.jsp">
	<ui:define name="title">Cybernetique en Nord</ui:define>
	<ui:define name="body">
		<f:view>
			<rich:tabPanel>
				<rich:tab label="Gameboard">
					<ui:include src="gameboard.jsp" />
				</rich:tab>
				<rich:tab label="Vision" rendered="#{VisionView.available}">
					<ui:include src="vision.jsp" />
				</rich:tab>
				<rich:tab label="Log">
					<ui:include src="log.jsp" />
				</rich:tab>
				<rich:tab label="Attributes">
					<ui:include src="attributes.jsp" />
				</rich:tab>
				<rich:tab label="Battery">
					<ui:include src="battery.jsp" />
				</rich:tab>
				<rich:tab label="Com">
					<ui:include src="com.jsp" />
				</rich:tab>
				<rich:tab label="ComSignatures">
					<ui:include src="comSignatures.jsp" />
				</rich:tab>
				<rich:tab label="LCD">
					<ui:include src="lcd.jsp" />
				</rich:tab>
				<rich:tab label="Motor">
					<ui:include src="motor.jsp" />
				</rich:tab>
				<rich:tab label="PID">
					<ui:include src="pid.jsp" />
				</rich:tab>
				<rich:tab label="Relay">
					<ui:include src="relay.jsp" />
				</rich:tab>
				<rich:tab label="Sonar">
					<ui:include src="sonar.jsp" />
				</rich:tab>
				<rich:tab label="Servo">
					<ui:include src="servo.jsp" />
				</rich:tab>
				<rich:tab label="Actions">
					<ui:include src="cup2012/actions2012.jsp" />
				</rich:tab>
				<!--				<rich:tab label="Launch">-->
				<!--					<ui:include src="launch.jsp" />-->
				<!--				</rich:tab>-->
				<rich:tab label="Tools">
					<ui:include src="tools.jsp" />
				</rich:tab>
				<rich:tab label="Chart">
					<ui:include src="chart.jsp" />
				</rich:tab>
				<rich:tab label="Trajectory"
					rendered="#{not empty TrajectoryRecoderView}">
					<ui:include src="trajectory.jsp" />
				</rich:tab>
				<rich:tab label="Services">
					<ui:include src="services.jsp" />
				</rich:tab>
				<rich:tab label="VideoRecorder"
					rendered="#{not empty VideoRecorderView}">
					<ui:include src="videoRecorder.jsp" />
				</rich:tab>
			</rich:tabPanel>
		</f:view>
	</ui:define>
</ui:composition>
</body>
</html>
