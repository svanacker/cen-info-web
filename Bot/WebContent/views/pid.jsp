<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<c:forEach var="type" items="#{PIDView.types}">
		<rich:panel>
			<f:facet name="pid">
				<h:outputText value="PID #{type.name}" />
			</f:facet>
			<h:outputText value="PID #{type.name}" />
			<h:panelGrid columns="#{PIDView.instructionCount * 2}">
				<c:forEach var="instructionType" items="#{PIDView.instructionTypes}">
					<rich:panel>
						<h:outputText value="PID #{instructionType.name}" />
						<h:panelGrid columns="2" border="0"
							style="border: 1px solid black;">
							<h:form id="pid#{type.index}_#{instructionType.index}">
								<h:outputText value="P" />
								<rich:inputNumberSlider minValue="0" value="#{PIDView.engine.pidParametersData[instructionType.index][type.index].p}" maxValue="255" step="1">
									<a4j:support event="onchange" reRender="" actionListener="#{MotorView.processAction}" id="test" />
								</rich:inputNumberSlider>
								<h:outputText value="I" />
								<rich:inputNumberSlider minValue="0" value="#{PIDView.engine.pidParametersData[instructionType.index][type.index].i}" maxValue="255" step="1">
									<a4j:support event="onchange" reRender="" action="#{PIDView.update}" />
								</rich:inputNumberSlider>
								<h:outputText value="D" />
								<rich:inputNumberSlider minValue="0" value="#{PIDView.engine.pidParametersData[instructionType.index][type.index].d}" maxValue="255" step="1">
									<a4j:support event="onchange" reRender="" action="#{PIDView.update}" />
								</rich:inputNumberSlider>
								<h:outputText value="MaxI" />
								<rich:inputNumberSlider minValue="0" value="#{PIDView.engine.pidParametersData[instructionType.index][type.index].maxI}" maxValue="255" step="1">
									<a4j:support event="onchange" reRender="" action="#{PIDView.update}" />
								</rich:inputNumberSlider>

								<center>
									<a4j:commandButton
										id="refresh_#{type.index}_#{instructionType.index}"
										image="../images/arrows/refresh.png" value="Refresh"
										actionListener="#{PIDView.processAction}" reRender="pid" />
								</center>
							</h:form>
						</h:panelGrid>
					</rich:panel>
				</c:forEach>
			</h:panelGrid>
		</rich:panel>
	</c:forEach>
	<br />
	<h:form>
		<h:outputText value="Refresh All Pids : " />
		<a4j:commandButton image="../images/arrows/refresh.png"
			value="Refresh" action="#{PIDView.refreshAllPids}" reRender="header" />
	</h:form>
	<rich:panel>
		<f:facet name="endMotion">
			<h:outputText value="End Motion Detection Parameter" />
		</f:facet>
		<h:panelGrid columns="2" border="0" style="border: 1px solid black;">
			<h:panelGrid width="400">
				<h:form id="absDeltaPositionIntegralFactorThreshold">
					<h:outputText
						value="Abs Delta Position Integral Factor Threshold : " />
					<rich:inputNumberSlider minValue="0"
						value="#{PIDView.endDetectionParameter.absDeltaPositionIntegralFactorThreshold}"
						maxValue="255" step="1" width="600">
						<a4j:support event="onchange" reRender="" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:panelGrid width="400">
				<h:form id="maxUIntegralFactorThreshold">
					<h:outputText value="Max U Integral Factor Threshold : " />
					<rich:inputNumberSlider minValue="0"
						value="#{PIDView.endDetectionParameter.maxUIntegralFactorThreshold}"
						maxValue="255" step="1" width="600">
						<a4j:support event="onchange" reRender="" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:panelGrid width="400">
				<h:form id="maxUIntegralConstantThreshold">
					<h:outputText value="Max U Integral Constant Threshold : " />
					<rich:inputNumberSlider minValue="0"
						value="#{PIDView.endDetectionParameter.maxUIntegralConstantThreshold}"
						maxValue="255" step="1" width="600">
						<a4j:support event="onchange" reRender="" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:panelGrid width="400">
				<h:form id="timeRangeAnalysis">
					<h:outputText value="Time Range Analysis : " />
					<rich:inputNumberSlider minValue="0"
						value="#{PIDView.endDetectionParameter.timeRangeAnalysis}"
						maxValue="255" step="1" width="600">
						<a4j:support event="onchange" reRender="" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
			<h:panelGrid width="400">
				<h:form id="noAnalysisAtStartupTime">
					<h:outputText value="No Analysis At Startup Time : " />
					<rich:inputNumberSlider minValue="0"
						value="#{PIDView.endDetectionParameter.noAnalysisAtStartupTime}"
						maxValue="255" step="1" width="600">
						<a4j:support event="onchange" reRender="" />
					</rich:inputNumberSlider>
				</h:form>
			</h:panelGrid>
		</h:panelGrid>
		<h:form>
			<h:outputText value="Refresh All End Motion Parameters : " />
			<a4j:commandButton image="../images/arrows/refresh.png"
				value="Refresh" action="#{PIDView.refreshEndMotionParameters}" reRender="endMotion" />
		</h:form>
	</rich:panel>
</ui:composition>
