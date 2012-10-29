<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ch="http://sourceforge.net/projects/jsf-comp">

	<ch:chart id="chart1" datasource="#{ChartView.pieDataset}"
		type="pie" is3d="true" antialias="true" title="Test"
		xlabel="X Label" ylabel="Y Label" height="300" width="400"></ch:chart>
		
		<p/>
		
	<ch:chart id="chart2" datasource="#{ChartView.XYDataset}"
		type="xyline" is3d="true" antialias="true" title="Classement de Cybernétique en Nord"
		xlabel="Années" ylabel="Place" height="300" width="400"></ch:chart>

</ui:composition>
