<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ch="http://sourceforge.net/projects/jsf-comp">
	
	<rich:panel id="pidMotionData">
		<h:form>
			<h:selectOneMenu id="selectCount" value="1" valueChangeListener="#{ChartView.onChange}" >
 				<f:selectItems value="#{ChartView.motionDataItems}" />
				<a4j:support event="onchange" reRender="pidMotionData,chart" />
			</h:selectOneMenu>
			<h:commandButton actionListener="#{ChartView.processAction}" id="addMotionData" value="Add statistic" />
			<h:commandButton actionListener="#{ChartView.processAction}" id="addSampleMotionData" value="Add Sample statistic" />
			<h:commandButton actionListener="#{ChartView.processAction}" id="clear" value="Clear" />
		</h:form>
	</rich:panel>

	<rich:panel id="chart">
		<ch:chart id="chart1" datasource="#{ChartView.pieDataset}"
			type="pie" is3d="true" antialias="true" title="Test"
			xlabel="X Label" ylabel="Y Label" height="300" width="400"></ch:chart>
			
		<ch:chart id="chart2" datasource="#{ChartView.XYDataset}"
			type="xyline" is3d="true" antialias="true" title="Classement de Cybernétique en Nord"
			xlabel="Années" ylabel="Place" height="300" width="400"></ch:chart>
			
			<p/>
		<ch:chart id="positionControl" datasource="#{ChartView.positionDataset}"
			type="xyline" is3d="true" antialias="true" title="Position control"
			xlabel="pidTime" ylabel="value" height="300" width="800"></ch:chart>
			
		<ch:chart id="speedControl" datasource="#{ChartView.speedDataset}"
			type="xyline" is3d="true" antialias="true" title="Speed control"
			xlabel="pidTime" ylabel="value" height="300" width="800"></ch:chart>
	</rich:panel>

</ui:composition>
